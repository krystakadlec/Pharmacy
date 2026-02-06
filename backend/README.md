# TJV_semestralni_prace

Téma semestrální práce je online lékarna, jenž bude obsahovat 3 entity:
- Patient
- Medication
- Prescription

V lékárně si pacient může vytvářet (Prescription), do kterých si může dávat jím chtěné léky.
Na již vytvořené předpisy se pacient může pokusit aplikovat slevu, která se mu aplikuje, má-li na ni nárok.
V minulosti vytvořené předisy může pacient volně mazat.

Vztah M:N bude mezi vazbami Prescription a Medication (tedy v Prescription může být víc léků naráz a zároveň Medication může být předepsán na více Prescription najednou).

# Business operace

Lide starší 60 let mají nárok na 20% slevu z celkové ceny prescription o kterou si budou moci zažádat stiskem tlačítka. 


# SQL dotaz

Všechny léky, které si daný pacient kdy objednal.
