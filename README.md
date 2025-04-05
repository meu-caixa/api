# requirement
- java 17
- docker

# run postgres
Before running the application, you need to have a PostgreSQL database running.
You can use Docker to run a PostgreSQL container with the following command:

```bash
docker run --rm -e POSTGRES_DB=meucaixa -e POSTGRES_HOST_AUTH_METHOD=trust -d -p 5432:5432 postgres
```

# To Do List
- [ ] add pipeline steps build, list, test ...
- [ ] add swagger
- [ ] add spring security
- [ ] add role scope
- [ ] add user scope
....