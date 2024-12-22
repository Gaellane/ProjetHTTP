echo "Lancement de l'installation"

echo "Decompresion du fichier"
sudo tar -xJf ProjetHTTP.tar.xz -C /opt/

echo "Installation de java"
sudo apt install default-jre

echo "Installation de javac"
sudo apt install default-jdk

cd /opt/ProjetHTTP

echo "Installation de l'application"
./backup.sh

rm -rf ./Code
echo "installation terminer"

