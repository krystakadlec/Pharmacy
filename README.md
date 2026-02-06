# Pharmacy

Webová aplikace simulující jednoduchou pro správu lékárny, pacientů a receptů.
Demonstruje třívrstvou architekturu v Javě a plnou kontejnerizaci.

## Funkcionalita:
- Vytvoření účtu: Možnost zaregistrovat si účet v lékárně, v rámci kterého si následně můžete vytvářet předpisy
- Správa receptů: Vytváření objednávek a přidávání léků (vazba M:N).
- Zažádání si o slevu: Po vytvoření objednávky, si můžete zažádat o slevu na předpisu (nárok na 20% slevu mají ale pouze senioři (60+)).
- Historie: Přehled a filtrace objednaných léků.

## Spuštění
Ke spuštění stačí pouze Docker.

**1. Stáhni projekt**
    do terminálu zadej:
```bash
git clone <url-repozitare>
cd pharmacy
```
**2. Spusť aplikaci**    
```bash
docker compose up --build
```
**3. Otevři v prohlížeči: http://localhost:8081**
