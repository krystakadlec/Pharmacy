# Pharmacy

Webová aplikace simulující jednoduchou správu lékárny, zejména pacientů a jejich receptů.
Demonstruje třívrstvou architekturu v Javě a plnou kontejnerizaci.

## Funkcionalita:
- Vytvoření účtu: Možnost zaregistrovat si účet v lékárně, v rámci kterého si následně můžete vytvářet předpisy
- Správa receptů: Vytváření objednávek, pomocí přidávání léků.
- Zažádání si o slevu: Po vytvoření objednávky, si můžete zažádat o slevu na předpisu (nárok na 20% slevu mají ale pouze senioři (60+)).
- Historie: Přehled všech objednaných léků.

## Spuštění
Ke spuštění stačí pouze Docker.

**1. Stáhněte projekt**
do terminálu zadejte:
```bash
git clone https://github.com/krystakadlec/Pharmacy.git
cd Pharmacy
```
**2. Spusťte aplikaci**    
```bash
docker compose up --build
```
**3. Otevřete v prohlížeči: http://localhost:8081**

## Použité technologie
- **Backend:** Java, Spring Boot, Gradle
- **Databáze:** PostgreSQL
- **Frontend:** Thymeleaf, HTML, CSS, JavaScript, Java
- **Kontejnerizace:** Docker + Docker Compose
