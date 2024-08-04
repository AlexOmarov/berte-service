# Berte Service

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=AlexOmarov_auth-service&metric=coverage)](https://sonarcloud.io/summary/new_code?id=AlexOmarov_auth-service)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AlexOmarov_auth-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=AlexOmarov_auth-service)

## ����������
- [��������](#��������)
- [������������ �����������](#������������-�����������)
- [���������](#���������)
- [�������������](#�������������-code-quality-������������)
- [������������� ���������� �������](#�������������-����������-�������)
- [����������](#����������)

### ��������
TODO: �������� ������� �������� �������

������� ������� ������� � IntellijIDEA �� ������� �������������� �������� ��������� �����.   
������ ����� ���� ������������ � IDE � ������� ���� `File->New->Project from existing sources->Gradle`.

### ������������ �����������
������ �������� � ��������������� ��������� ������������:
- [Ktor](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Reactive Redis](https://developer.redis.com/develop/java/spring/rate-limiting/fixed-window/reactive/)
- [Reactive Postgres](https://github.com/pgjdbc/r2dbc-postgresql)

������ ������ ������������ ��������� ����� ����� � ����� [libs.versions.toml](gradle/libs.versions.toml)

### ���������
�������� ������ ���������� ������ � ��� �������� ������� ��������� Docker.
```bash
docker-compose up -d
```
Compose �������� � ���� ��������� �����������, ������ �� ������� �������� ��� ����������� �� localhost:
1. Postgres - localhost:5432
2. Redis - localhost:6379
3. Otel Collector - localhost:4317/4318/4319
4. ��� ������ - localhost:8080

#### ��������� ���������
������ ����� ��������������� � ��������, � �������������� ���������� ��������� ���� ������ docker-������.
��� ����� ���������� ���������� �������� ��������� � �������������� Docker compose �����, 
����� ����� ������� ��������� � �������� (service), 
������� ������ � ��������� ��� ���� ����� IDE, ���� ����� ��������� � docker compose ���� -
����������������� ������ build/context ���������� service � ������ ��������� ������.

### ������������� ���������� �������
`Healthcheck` API �������� �� `/health` ����
����� ��� ������� ������ �������� API ������� �� ������ `localhost:8080`.   
API ���������� ������� ������ Cbor � Json, ���������� � ����������� �� ���������� ����������.

### ������������� code-quality ������������
����� ������ ���������� � �������������� `build` ������ gradle detekt � ktlint �������� �������� �������������. 
Detekt ����� ����������� �� ����� `./build/app/reports/detekt`, `./build/api/reports/detekt`.  
����� ���� ����������� ��������� �������� ������� ��������
```bash  
.\gradlew detekt
```  

������������ � ��������� �������� ����� �������� ������������� ��� ������ ������� `build`. 
�������� ���������� ������������ kover, ������� � ���� ������� ���������� ������ JaCoCo.  
������ �� �������� (xml ��� sonar-������������ � html ��� ��������� ����������)  
����������� �� ����� `./build/app/reports/kover/report.xml`, `./build/app/reports/kover/html/index.html`.  
����� ���� ����������� ������� ������������ � ��������� �������� �������, ������ �������
```bash  
.\gradlew test koverPrintCoverage
```  
������� �������� ����� ����� �������� � � ����� IDE.  
��� ����� ���������� ������ ������� ���� �������� �� ����� test � ��������� ������ Run with Coverage.  
**�����!** ����� �� �������, ���� ������������ � ������ ��������� compose!

����������� Quality Gate ����������� � �������������� gradle ������� sonarqube. ������� ����������� ����� ���������:
```bash  
.\gradlew build
.\gradlew sonar -D"sonar.host.url"="<SONAR_HOST>" -D"sonar.token"="YOUR_TOKEN" -D"sonar.projectKey"="KEY" -D"sonar.organization"="ORG"
```  

��� ������ sonar � ������� gradle ������ ��������������� detekt ����� � kover ����� ����� �������� � �������.

### ����������
��� ������������ ����� ������� ����������� ��������� ���������� ������� � mavenLocal.  
��� ����� ������������ ������� publishToMavenLocal.

```bash  
.\gradlew publishToMavenLocal
```  
� ���������� ���������� ������� � .m2 ����� ������������ �������� �������� auth-service-api, ������� ����� ��������� ���  
����������� dto-������, proto-����� � ������ ������ ��� �������������� � �������� ����������.  