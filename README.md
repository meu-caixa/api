# Requisitos
- java 17
- docker
- otel agent - https://opentelemetry.io/docs/zero-code/java/agent/getting-started/

# Antes de rodar o projeto
Antes de rodar o projeto, você precisa ter um banco de dados PostgreSQL rodando.
```bash
docker run --rm -e POSTGRES_DB=meucaixa -e POSTGRES_HOST_AUTH_METHOD=trust -d -p 5432:5432 postgres
```

(Opcional) Você pode rodar o projeto com otel agent, para isso, baixe o jar do agente e configure o VM options:
```bash
-javaagent:{path_to_agent_jar}/opentelemetry-javaagent.jar
-Dotel.service.name=meu-caixa-api
-Dotel.metrics.exporter=otlp
-Dotel.logs.exporter=otlp
-Dotel.exporter.otlp.protocol=grpc
```
notas: o `{path_to_agent_jar}` deve ser o caminho onde você baixou o agente jar.