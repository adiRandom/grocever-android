# Deal Scraper

Acest repository conține codul pentru clientul de Android pentru Deal Scaper
Deal Scraper este o aplicație care îți scanează galeria după imagini cu bonuri fiscale, 
identifică produsele cumpărate și caută cele mai bune oferte pentru produsele pe care le-ai cumpărat.

Pentru realizarea acestui scop. aplicația se folosește de tehnici de învățare automată și web scraping.

Odată deschisă. aplicația cere permisiunea utilizatorului pentru a-i accesa galeria de imagini. Dacă acesta oferă permisiunea,
aplicațoa pornește un serviciu periodic care scanează galeria după noi imagini care conțin imagini cu bonuri. Detectarea obiectelor din imagini
pentru a determina dacă este sau nu o imagine de interes se realizează local prin intermediul unui model de ML de detecție de obiecte, 
folosind TenserFlow Lite.

Totodată Deal Scraper pune la dispoziția utilizatorilor o privire de ansamblu asupra cheltuielilor acestora și cât puteau economisii dacă foloseau ofertele recomandate de aplicație. Utilizatorii pot alege să vadă aceste statistici fie pe perioadă nelimitată, fie pentru o perioaă
de o săptămână aleasă de aceștia.

Aplicația de Android este construită folosind Jetpack Compose și arhitectura MVVM. 
De asemenea, Deal Scaper se folosește de următoarele API-uri native pentru scanarea galeriei utilizatorului

- Work Manager
- Content Provider

Alte biblioteci folosite:
- Material UI 3 pentru interfața aplicației
- Room pentru persistența datelor
- Retrofit pentru cereri HTTP
- Koin pentru dependency injection
- Coil pentru încărcarea de imagini remote

Detectarea textului din imagini și identificarea de oferte pentru produsele identificare este realizată de serviciile de backend Deal Scraper.
O dată detectate oferte, Deal Scraper se folosește de Firebase Cloud Messeging pentru a prezenta o notificare utilizatorului pentru a-i aduce 
la cunoștință existența unor noi oferte.

