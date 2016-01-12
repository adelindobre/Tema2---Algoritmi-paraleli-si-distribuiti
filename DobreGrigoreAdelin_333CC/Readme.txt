Tema2  - Algoritmi paraleli si distribuiti - 

Detalii de Implementare:

Am ales sa lucrez cu interfata ExecutorService.
Astfel, creez doua workpool-uri: una care va contine Taskurile Map, si alta care va contine taskurile Reduce.
Pentru fiecare document de procesat al carui nume este precizat in fisierul de intrare, numar cate blocuri de lungime fixa sunt(lungimea fiind data tot in fiserul de intrare) si creez atatea taskuri Map, daugand la final un task corespunzator blocului cu octetii ramasi de citit. Tot aici creez si un task Reduce, specific documentului.
Taskurile de Map si Reduce sunt continute in cate o lista separata de Callables.
Asadar, clasele Map si Reduce implementeaza interfata Callable.
Rezultatele de la Map le obtin prin apelarea executorService.invokeAll(callables) care returneaza o lista de obiecte de tip Future care returneaza un obiect de tipul BlockResult prin apelul get().
Fiecare reduce in parte isi va actualiza lista cu rezultate de la Map, specifice documentului

Functionare Map:
Testez daca offsetul este 0, adica ma aflu la inceputul documentului. In caz afirmativ citesc un bloc de dimensiune fixa. Altfel, citesc un caracter anterior fata de offsetul curent, pentru a vedea daca ma aflu in mijlocul unui cuvant sau nu(adica daca este un caracter alfanumeric sau un delimitator de sir). Apoi citesc un bloc intreg de "block_size" octeti. Daca nu ma aflu la finalul fisierului, adica offsetul de inceput plus cat am citit la pasul curent nu este egal cu lungimea totala a fisierului, adaug cate un caracter pana dau de un delimitator. 
Delimitarea blocului se face folosind un stringTokenizer, de care ma folosesc pentru a construi hashul de asocieri lungime  -  numar_aparitii, precum si vectorul de cuvinte maximale.

Functionare Reduce:
O prima etapa reprezinta combinarea. Astfel este parcursa lista de BlockResult din fiecare Map, si se combina toate hashurile corespunzatoare fiecarui bloc de octeti intr-un singur hash specific clasei Reduce. La fel se procedeaza si in cadrul cuvintelor maximale. Numele acestor colectii sunt: "final_hs" si  "max_words".
Apoi urmeaza etapa de procesare pentru calcularea range-ului.. Se obeserva metoda folosita pentru a itera prin hash si anume: obtinerea mai intai a unui set de keyset, parcurs ulterior cu ajutorul unui iterator().
Functia suprascrisa call, returneaza un Finalresult ce va contine range-ul, numele fisierului, numarul de cuvinte maximale diferite si dimensiunea maxima.
Rezultatul Reduce-urilor este din noou obtinut cu ajutorul obiectelor de tip Future in ordinea ceruta in cerinta.

Tema a fost implementata si testata pe mediul Netbeans in Windows 8.1, folosind versiunea de java 1.8. Tema ruleaza corect pentru toate cele patru teste, iar scalabilitatea este mai evidenta la testull 4, acolo unde cu cat cresc numarul de threaduri, cu atat timpul devine mai scurt.


Adelin Grigore Dobre 333CC 

