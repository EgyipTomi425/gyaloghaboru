# Sakkprogram: gyalogháború

### Menyhárt Tamás

A gyalogháború lényege, hogy felpakoljuk a figurákat a sakk szabályai szerint, de csak a gyalogokat. Lépünk a jatéknak megfelelően, és az nyer, aki előbb eljut az ellenfél alapsorára vagy leüti az ellenfél összes bábúját.

Jelenleg nem fog rendelkezni gépi játékossal, de ez a jövőben változhat.

Később tervben van több figura, és akár a rendes sakkjáték is.

How to run: (minimum JDK 17 and maven 3.7)
"""bash
git clone https://github.com/EgyipTomi425/gyaloghaboru.git
cd gyaloghaboru
mvn clean install
mvn compile
mvn exec:java
"""

Egy lejátszott játék után az új játék megnyomásakor mentést is készít az eredményekről, több lejátszott játék után pedig az eredménytáblán lesznek rekordok. 

Források: Jeszy75 (Jeszenszky Péter) tárolója.
