# bureauworks-backend
BWM challenge backend

# Depêndencias para rodar o projeto

- Java: v21
- Postgresql ou Docker com Imagem Postgresql

# Como executar o projeto :

1. Baixe o posgresql ou crie o docker com a imagem do mesmo com o comando :

```bash
docker run -d --name postgresql --restart always -e POSTGRES_PASSWORD=123456 -p 5432:5432 -e TZ=America/Sao_Paulo -v /opt/docker/volumes/postgresql/9/data:/var/lib/postgresql/data postgres:16
```
OBS: Caso já tenha um docker com imagem do banco, basta usar a senha local dentro do arquivo application.properties no campo esperado

OBS2: Execute o arquivo no 'script.sql' no banco postgresql

2. Clone o projeto 

```bash
git@github.com:NilvanPeres/bureauworks-backend.git
```

3. Na sua IDE preferida, execute a classe principal do projeto: `BureauworksApplication.java`

4. Aplicação ficará disponível em : [http://localhost:8080](http://localhost:8080/api/v1)

5. Documentação disponível [aqui](http://localhost:8080/api/v1/swagger-ui/index.html)

