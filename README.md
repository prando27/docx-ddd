## Projeto de CRUD baseado nos conceitos de DDD (Domain Driven Design)

## Stack
 - Spring Boot
 - Spring MVC
 - Spring Data JPA
 - Hibernate
 - Hibernate-Types
 - Lombok

## DDD Concepts
**Application**
 - Application Services
 - Use cases da aplicação
 - Regras da aplicação vão aqui (não regras de negócio, isso sempre é feito pelos objetos de domain)
 - Faz a orquestração com os objetos da camada de domain, não tem regra de negócio
 - Sabe o que é uma transação (Transactional, Unit of Work, etc)
 - Contrato de dados (entrada e saída ou os DTOs) entre cliente e servidor são feitos aqui
   - Conversões entre objetos de domain e DTOS também!
 - "Conversa" com valueobjects, aggregates e outros application services
 - Tem um "pezinho" na infraestrutura, exemplo acesso ao S3 ou até mesmo ser um RestController do Spring MVC
   - Se o Use case for iniciado somente via API REST, faz sentido ele ser direto o RestController
   - Tentar não quebrar em camadas quando desnecessário


**Domain**
 - Toda a regra de negócio vai aqui!
 - DomainEvents
 - ValueObjects, objetos imutáveis que tem identidade baseada nos atributos
 - Entities - objetos mutáveis que tem identidade a partir de um id (Geralmente auto-inc em bancos relacionais)
 - Aggregates
   - Conjunto de entities e value objects que determina um conceito ou abstração na regra de negócio
   - Possui um AggregateRoot que é a entidade que dá Id ao Aggregate
   - São responsáveis por criar DomainEvents (mas não por dispará-los, quem faz isso é a camada de infraestrutura)
 - Domain Services (Regras que envolvem mais de uma entity ou aggregate e não se encaixa em nenhuma das mesmas/mesmos)
 - Todos os objetos são bem encapsulados, não é possível cria-los ou obte-los em um estado inconsistente


**Infrastructure**
 - Toda a parte "baixo nível vai aqui"
   - baixo nível aqui significa que interage com coisas externas como banco, protocolo http, filas etc
 - Implementação de repositories
 - Controllers HTTP, erros 4XX, 5XX são feitos nessa camada com base no que recebe de um application service
 - Listeners de repositories para implementar two way dispatch de eventos de domínio

## Conceitos e ideias utilizados
- Fail Fast
- CQS (Command Query Separation)
- Primitive Obsession (Esse é para ser evitado!)
- ValueObject

## Etapas para criação de um novo tipo de documento

**Domain**
1. Criar um novo DocumentType e a constant String com o nome
2. Criar um novo documento que estende de Document (Implementar os métodos abstratos)
 2.1. DiscriminatorValue = constante do novo DocumentType
 2.2. Criar um backing object para os atributos (ValueObject)
 2.3. Criar os getters
 2.4. Criar um atributo attributes do tipo do backing object
 2.5. Criar um método estático create

**ApplicationService**
1. Criar um novo DTO que estende de CreateDocumentDto com os campos do documento
2. Adicionar o novo DTO na lista de CreateDocumentDto (JsonSubTypes)
3. Criar uma nova implementação de CreateStrategy para o documento (Component do Spring)



## Melhorias

## Problemas sem solução
1. Como atualizar o tipo de documento entre RG, CNH e RNE?
2. O Spring faz rollback de transação somente em caso de exceção, com o uso do Result isso deve ser repensado.
3. Como usar o mesmo tipo de documento para contextos diferentes (A mesma pasta pode ser usada para diferentes contextos).
  - Abrir um pouco o contrato do objeto e usando NullObject para evitar NPE





Alguns documentos tem campos obrigatórios por contexto, como validar isso a nível de API?

Exemplo:

PERSONAL_DATA (Um por contexto!!!!!)

PP = Nome, CPF e Email
IQ = Nome, CPF, Estado Civil, Celular, Telefone, Email
MR = Nome, Data de Nascimento/ (Se for maior de idade) RG, CPF, Celular, Email

Nome

OWNER_PERSONAL_DATA
TENANT_PERSONAL_DATA
RESIDENT_PERSONAL_DATA

1 - O que é comum entre eles pode ser sempre atualizado!
Comum: Nome,


Opções:
1 - Modelo possui diferentes DocumentTypes (OWNER_PERSONAL, TENANT_PERSONAL, RESIDENT_PERSONAL)
 1.1. Contra: Duplicação de dados
 1.2. Contra: Aproveitamento de documentos precisa ser feito do lado client
  1.2.1. Tela de IQ: Client ve que não tem TENANT_PERSONAL mas tem OWNER_PERSONAL e já popula o store com os campos encontrados (Nome e Email por ex)
 1.3. Pró: Mais fácil de implementar, seria apenas outro tipo de documento
 1.4. Pró: Modelo fica ajustado as necessidades do tipo de documento, continua forte, não precisa de checagem para lidar com os campos (Always Valid)
 1.5. Pró: Consumidores são simples pois seria um DocumentProcessor para cada tipo novo

2 - ApplicationService tem uma camada de contexto (Pode ser um header ou endpoint diferente) para o tipo de documento, nesse caso o PERSONAL_DATA possui todos os dados possíveis
 2.1 - Validação é feita de maneira contextual, se chamou a api no contexto PP, valida x campos, contexto IQ y campos e por ai vai
 2.2. Pró: Não tem duplicação de código, tudo fica armazenado no mesmo DocumentType PERSONAL_DATA
 2.3. Pró: Aproveitamento de documento entre contexto é mais fácil
  2.3.1. Ex: O client já tem o PERSONAL_DATA preenchido em outro contexto e pode exigir somente o preenchimento dos dados restantes
 2.4. Contra: Modelo fica fraco, qualquer uso dos campos de PERSONAL_DATA precisam de um Optional ou if != null (Não é Always Valid)
 2.5. Contra: Lógica de atualização fica mais perigosa, não pode apenas trocar os attributes e sim fazer um merge para não perder dados de outro contexto
 2.6. Contra: Consumidor prejudicado pois teria que lidar com lógica de atributos nulos (Pode não ser um problema)
  2.6.1. Sempre ficar validando se os campos daquele contexto estão preenchidos



