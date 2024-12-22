echo "construction du backup"
sudo tar -xJf bkp.tar.xz -C .
javac -d bin bkp/*.java

echo "deplacement du configuration de base"
cp bkp/config.txt config/config.txt

sudo rm -rf bkp
echo "application remis a son defaut"
