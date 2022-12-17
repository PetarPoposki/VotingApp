# VotingApp
Voting Android App


1. Aplikacijata ima registracija i najava.
2. Mozeme da se najavime kako admin ili user
3. Pri najava so email: "admin@project.com" pass: "Admin123", aplikacijata ne nosi na Adminoverview aktivnosta. Tuka, adminot ima realtime pregled na site glasanja
so nivnite glasovi.
4. Adminot moze da kreira novo glasanje, so toa sto kje vpise ime na glasanjeto, pa potoa kje stisne na kopceto za Create Poll.
5. So pritiskanje na ikonceto za mapa, aplikacijata ne nosi do Recycler View pogled na lokaciite od kade sto site korisnici glasale.
6. Vo AdminActivity, vo poleto za prasanje se vnesuva prasanje, a vo poleto za odgovor se vnesuva odgovor za istoto prasanje. Moze kolku sakame prasanja za edno glasanje,
so bilo kolku odgovori za sekoe.
7. Koga sme zadovolni so brojot na prasanja, so scroll nadolu vnesuvame vremetraenje na poll-ot, i stiskame na kopceto za da se dodade i aktivira glasanjeto.
8. Pri pritiskanje na kopceto za create, se isprakja notifikacija na site users preku Firebase Messaging Service. Ovaa notifikacija pristignuva bez razlika
dali aplikacijata e vo foreground ili background (za demonstracija, taa pristignuva i kaj adminot, no toa moze da se smeni po potreba).
9. Ako se najavime kako user, aplikacijata ne nosi na prasanjata na koi sto moze da glasame, i potoa pocnuva tajmer. Dokolku iscezne tajmerot, prasanjata isceznuvaat.
Dokolku glasame za nekoe prasanje, prasanjeto isceznuva. Pri odnovo pustanje na aplikacijata, ne ni e dozvoleno povtorno da glasame na prasanje, dokolku
za istoto e ili isteceno vremeto, ili sme glasale vekje za nego.
10. Dokolku pritisneme na MOI GLASANJA, odime na prikaz za rezultati na prasanja na koi sto vekje sme glasale. Tuka nema da se prikazat prasanja na koi sto ne sme glasale.
11. Dokolku vo aktivnosta za glasanje, ja svrtime orientacijata vo Landscape, so pomos na fragmenti se prikazuva i ResultsActivity, paralelno so glasanjeto.
Funkcionalni se i dvata fragmenti (mozeme i da glasame, i da skrollame niz rezultati).
