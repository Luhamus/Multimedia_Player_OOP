## Veebiserver
veebiserver, mis suudab vastu võtta browserilt tulevaid päringuid ja neile vastata. 
### Funktsionaalsus
- [ ] suudab browseri saadetud päringute sünktaksist aru saada
    - [x] Request Line Parse 
    - [x] Headers Parse 
    - [ ] Body Parse
    - [x] Url parameters
- [ ] suudab vastuseks ketta pealt faile serveerida
    - [x] Puhas Html
    - [ ] Pildid jms
- [ ] suudab vastu võtta browseris täidetud ankeetide andmeid (html forms; tekstilüngad ja uploaditud failid)
- [x] suudab mitu päringut parallelselt töödelda
- [ ] suudab päringutele dünaamiliselt vastuseid koostada (vastavalt ankeeti sisestatud andmetele / aadressi lisatud parameetritele)
- [ ] võimaldab pluginaid kirjutada, nii et dünaamiliselt vastuste koostamise funktsionaalsust saab kergelt lisada
- [ ] toetab lehele parooliga sisselogimist
