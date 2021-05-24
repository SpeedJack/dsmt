Aggiungere le librerie a glassfish

asadmin> add-library --type common /home/nicola/IdeaProjects/dsmt/DistributedAuctionSystem/libraries-glassfish/erlang-jinterface-1.10-1.jar
asadmin> add-library --type common /home/nicola/IdeaProjects/dsmt/DistributedAuctionSystem/libraries-glassfish/jinterface-1.6.1.jar

RIAVVIARE GLASSFISH

Provare a fare deploy dell'artifact EJB

Nel caso non funzioni ancora provare con questi comandi e poi a riavviare ancora glassfish
asadmin> add-library --type ext C:/Users/loren/IdeaProjects/dsmt/DistributedAuctionSystem/libraries-glassfish/erlang-jinterface-1.10-1.jar
asadmin> add-library --type ext C:/Users/loren/IdeaProjects/dsmt/DistributedAuctionSystem/libraries-glassfish/jinterface-1.6.1.jar

