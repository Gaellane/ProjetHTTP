function afficherNom() {
    var nom=document.getElementById("nom").value;
    var newHtml=document.getElementById("reponse").innerHTML;
    document.getElementById("reponse").innerHTML=("Bonjour "+nom);
}