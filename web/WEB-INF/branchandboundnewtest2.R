#Criteria
#Distance, Cost, Crowdedness, Alcohol Choice, PageRank

#Assume 10 bars. 
#Cost is a relative 1-10, Crowdedness likewise, Alcohol Choice is Liquor Preference 1-10 (Beer preference is then 10-alcohol choice), Pagerank is also 1-10.
#Distances is an nxn matrix 

#User Defined Parameters
#nobot=10  #max number of bars on tour
#Preferences 1-10
#costpref=10
#crowdpref=4
#alcpref=2#
#startingbar="Inferno"
#Pagerank is outside of user control.
nobot=nobot-1
#Lets generate sample data matrices:
#bars=c('Allen St. Grill','Bar Bleu','The Brewery','Café 210','Chilis','Chrome','Chumleys','Darkhorse Tavern','Gingerbread Man','Indigo','Inferno','Kildares', 'Levels','Lions Den','Local Whiskey',
#'Mad Mex','The Phyrst','Bill Pickles Tap Room', 'The Rathskeller','Rotellis','Rumors Lounge','The Saloon','The Shandygaff','The Tavern Restaurant','Z Bar @ The Deli','Zenos') 
thebars=structure(list(Allen.St..Grill = c(0L, 600L, 550L, 160L, 120L, 
350L, 14L, 110L, 600L, 25L, 500L, 950L, 650L, 611L, 212L, 380L, 
212L, 49L, 170L, 400L, 450L, 500L, 210L, 220L, 550L, 6L), Bar.Bleu = c(600L, 
0L, 400L, 290L, 650L, 900L, 650L, 550L, 170L, 650L, 120L, 400L, 
96L, 19L, 620L, 591L, 620L, 513L, 500L, 350L, 1000L, 160L, 400L, 
400L, 130L, 600L), The.Brewery = c(550L, 400L, 0L, 550L, 140L, 
450L, 400L, 280L, 300L, 400L, 400L, 800L, 500L, 400L, 190L, 160L, 
190L, 350L, 230L, 140L, 750L, 400L, 190L, 180L, 350L, 400L), 
    Cafe.210 = c(160L, 290L, 550L, 0L, 290L, 280L, 150L, 280L, 
    750L, 140L, 650L, 1100L, 800L, 811L, 377L, 330L, 377L, 214L, 
    350L, 550L, 300L, 650L, 350L, 400L, 700L, 170L), Chilis = c(120L, 
    650L, 140L, 290L, 0L, 290L, 140L, 84L, 550L, 150L, 600L, 
    1000L, 710L, 615L, 83L, 251L, 83L, 75L, 230L, 350L, 450L, 
    550L, 300L, 300L, 550L, 120L), Chrome = c(350L, 900L, 450L, 
    280L, 290L, 0L, 350L, 400L, 750L, 350L, 850L, 1300L, 1002L, 
    900L, 362L, 471L, 362L, 370L, 500L, 600L, 270L, 850L, 600L, 
    600L, 800L, 400L), Chumleys = c(14L, 650L, 400L, 150L, 140L, 
    350L, 0L, 130L, 600L, 11L, 500L, 950L, 650L, 661L, 226L, 
    400L, 226L, 63L, 180L, 400L, 450L, 500L, 220L, 240L, 550L, 
    20L), Darkhorse.Tavern = c(110L, 550L, 280L, 280L, 84L, 400L, 
    130L, 0L, 500L, 140L, 500L, 950L, 658L, 517L, 159L, 270L, 
    159L, 63L, 146L, 270L, 453L, 485L, 215L, 230L, 459L, 106L
    ), Gingerbread.Man = c(600L, 170L, 300L, 750L, 550L, 750L, 
    600L, 500L, 0L, 600L, 110L, 550L, 240L, 148L, 506L, 477L, 
    506L, 520L, 400L, 250L, 950L, 78L, 400L, 350L, 52L, 600L), 
    Indigo = c(25L, 650L, 400L, 140L, 150L, 350L, 11L, 140L, 
    600L, 0L, 110L, 1000L, 650L, 661L, 237L, 410L, 237L, 74L, 
    190L, 400L, 450L, 550L, 230L, 250L, 550L, 31L), Inferno = c(500L, 
    120L, 400L, 650L, 600L, 850L, 500L, 500L, 110L, 110L, 0L, 
    450L, 140L, 142L, 600L, 571L, 600L, 594L, 350L, 270L, 1000L, 
    29L, 280L, 270L, 55L, 500L), Kildares = c(950L, 400L, 800L, 
    1100L, 1000L, 1300L, 950L, 950L, 550L, 1000L, 450L, 0L, 300L, 
    411L, 1000L, 1000L, 1000L, 950L, 800L, 700L, 1400L, 450L, 
    700L, 700L, 500L, 900L), Levels = c(650L, 96L, 500L, 800L, 
    710L, 1002L, 650L, 658L, 240L, 650L, 140L, 300L, 0L, 115L, 
    714L, 680L, 714L, 634L, 500L, 400L, 1100L, 160L, 400L, 400L, 
    190L, 600L), Lions.Den = c(611L, 19L, 400L, 811L, 615L, 900L, 
    661L, 517L, 148L, 661L, 142L, 411L, 115L, 0L, 600L, 571L, 
    600L, 594L, 500L, 300L, 1000L, 140L, 450L, 400L, 110L, 650L
    ), Local.Whiskey = c(212L, 620L, 190L, 377L, 83L, 362L, 226L, 
    159L, 506L, 237L, 600L, 1000L, 714L, 600L, 0L, 171L, 1L, 
    0L, 240L, 350L, 550L, 550L, 300L, 350L, 550L, 200L), Mad.Mex = c(380L, 
    591L, 160L, 330L, 251L, 471L, 400L, 270L, 477L, 410L, 571L, 
    1000L, 680L, 571L, 171L, 0L, 171L, 311L, 210L, 300L, 700L, 
    550L, 280L, 300L, 500L, 350L), The.Phyrst = c(212L, 620L, 
    190L, 377L, 83L, 362L, 226L, 159L, 506L, 237L, 600L, 1000L, 
    714L, 600L, 1L, 171L, 0L, 157L, 240L, 350L, 550L, 550L, 300L, 
    350L, 550L, 200L), Bill.Pickles.Tap.Room = c(49L, 513L, 350L, 
    214L, 75L, 370L, 63L, 63L, 520L, 74L, 594L, 950L, 634L, 594L, 
    0L, 311L, 157L, 0L, 190L, 350L, 450L, 550L, 230L, 240L, 500L, 
    43L), The.Rathskeller = c(170L, 500L, 230L, 350L, 230L, 500L, 
    180L, 146L, 400L, 190L, 350L, 800L, 500L, 500L, 240L, 210L, 
    240L, 190L, 0L, 210L, 600L, 350L, 69L, 84L, 400L, 160L), 
    Rotellis = c(400L, 350L, 140L, 550L, 350L, 600L, 400L, 270L, 
    250L, 400L, 270L, 700L, 400L, 300L, 350L, 300L, 350L, 350L, 
    210L, 0L, 700L, 240L, 170L, 160L, 220L, 350L), Rumors.Lounge = c(450L, 
    1000L, 750L, 300L, 450L, 270L, 450L, 453L, 950L, 450L, 1000L, 
    1400L, 1100L, 1000L, 550L, 700L, 550L, 450L, 600L, 700L, 
    0L, 900L, 650L, 700L, 900L, 450L), The.Saloon = c(500L, 160L, 
    400L, 650L, 550L, 850L, 500L, 485L, 78L, 550L, 29L, 450L, 
    160L, 140L, 550L, 550L, 550L, 550L, 350L, 240L, 900L, 0L, 
    300L, 290L, 25L, 500L), The.Shandygaff = c(210L, 400L, 190L, 
    350L, 300L, 600L, 220L, 215L, 400L, 230L, 280L, 700L, 400L, 
    450L, 300L, 280L, 300L, 230L, 69L, 170L, 650L, 300L, 0L, 
    15L, 350L, 200L), The.Tavern.Restaurant = c(220L, 400L, 180L, 
    400L, 300L, 600L, 240L, 230L, 350L, 250L, 270L, 700L, 400L, 
    400L, 350L, 300L, 350L, 240L, 84L, 160L, 700L, 290L, 15L, 
    0L, 300L, 220L), Z.Bar...The.Deli = c(550L, 130L, 350L, 700L, 
    550L, 800L, 550L, 459L, 52L, 550L, 55L, 500L, 190L, 110L, 
    550L, 500L, 550L, 500L, 400L, 220L, 900L, 25L, 350L, 300L, 
    0L, 550L), Zenos = c(6L, 600L, 400L, 170L, 120L, 400L, 20L, 
    106L, 600L, 31L, 500L, 900L, 600L, 650L, 200L, 350L, 200L, 
    43L, 160L, 350L, 450L, 500L, 200L, 220L, 550L, 0L)), .Names = c("Allen.St..Grill", 
"Bar.Bleu", "The.Brewery", "Cafe.210", "Chilis", "Chrome", "Chumleys", 
"Darkhorse.Tavern", "Gingerbread.Man", "Indigo", "Inferno", "Kildares", 
"Levels", "Lions.Den", "Local.Whiskey", "Mad.Mex", "The.Phyrst", 
"Bill.Pickles.Tap.Room", "The.Rathskeller", "Rotellis", "Rumors.Lounge", 
"The.Saloon", "The.Shandygaff", "The.Tavern.Restaurant", "Z.Bar...The.Deli", 
"Zenos"), class = "data.frame", row.names = c(NA, -26L))
bars=names(thebars)
Costvalues=
structure(c(5, 5, 2.5, 2.5, 5, 10, 2.5, 5, 2.5, 2.5, 5, 5, 2.5, 
2.5, 5, 5, 2.5, 2.5, 2.5, 5, 5, 5, 2.5, 5, 5, 2.5), .Names = c("Allen.St..Grill", 
"Bar.Bleu", "The.Brewery", "Cafe.210", "Chilis", "Chrome", "Chumleys", 
"Darkhorse.Tavern", "Gingerbread.Man", "Indigo", "Inferno", "Kildares", 
"Levels", "Lions.Den", "Local.Whiskey", "Mad.Mex", "The.Phyrst", 
"Bill.Pickles.Tap.Room", "The.Rathskeller", "Rotellis", "Rumors.Lounge", 
"The.Saloon", "The.Shandygaff", "The.Tavern.Restaurant", "Z.Bar...The.Deli", 
"Zenos"))
#want to minimize this always; 
Crowdvalues=rpois(length(bars),4);names(Crowdvalues)=bars
Alcvalues=rpois(length(bars),4);names(Alcvalues)=bars
Pagerankvalues=rpois(length(bars),1);names(Pagerankvalues)=bars

#Making distances:
#dvals=matrix( rnorm(length(bars)*length(bars),mean=.2,sd=.4), length(bars), length(bars)) #helps create distances
#distances=as.data.frame(as.matrix(dist(dvals,upper=T))) #Keep the upper because we dont have to worry about switching i and j.
distances=thebars
row.names(distances)=bars
#names(distances)=bars
#row.names(distances)=bars
# Lets Sort the user preference.
prefrank=cbind(c(costpref,alcpref,crowdpref),c("Cost","Alc","Crowd"))
rankedpreferences=as.character(as.data.frame(prefrank)[order(as.data.frame(prefrank)$V1),]$V2)


two_bar_bakeoff=function(userprefs,bar){ 
#find the nearest two bars
firstbar=names(sort(distances[bar,])[2]) #first is the bar itself
secondbar=names(sort(distances[bar,])[3]) #first is the bar itself
firstbarcost=Costvalues[[firstbar]]; secondbarcost=Costvalues[[secondbar]]
firstbarcrowd=Crowdvalues[[firstbar]]; secondbarcrowd=Crowdvalues[[secondbar]]
firstbaralc=Alcvalues[[firstbar]]; secondbaralc=Alcvalues[[secondbar]]
firstbarpagerank=Pagerankvalues[[firstbar]]; secondbarpagerank=Pagerankvalues[[secondbar]]

#lets make it really greedy by minimizing the weighted sum of the objectives at the branch.
#If not, rankedpreferences is stored as a list, and userprefs is a dummy input to adjust this
costfactor=-1.2 #20% percent more important
pagerankfactor=-3
firstbarscore=firstbarcrowd*crowdpref+firstbaralc*alcpref+firstbarcost*costfactor*costpref#+pagerankfactor*firstbarpagerank  #assume higher cost rank means more expensive
secondbarscore=secondbarcrowd*crowdpref+secondbaralc*alcpref+secondbarcost*costfactor*costpref#first+pagerankfactor*secondbarpagerank
if(min(firstbarscore,secondbarscore)==firstbarscore){return(firstbar)}else
return(secondbar)
}



two_bar_bakeoff_force=function(userprefs,bar,pagerankedbar,nearestbar,Tabu){ 
#find the nearest two bars
firstbar=names(sort(distances2[bar,])[2]) #first is the bar itself
secondbar=names(sort(distances2[bar,])[3]) #first is the bar itself
firstbarcost=Costvalues[[firstbar]]; secondbarcost=Costvalues[[secondbar]]
firstbarcrowd=Crowdvalues[[firstbar]]; secondbarcrowd=Crowdvalues[[secondbar]]
firstbaralc=Alcvalues[[firstbar]]; secondbaralc=Alcvalues[[secondbar]]
firstbarpagerank=Pagerankvalues[[firstbar]]; secondbarpagerank=Pagerankvalues[[secondbar]]
#lets make it really greedy by minimizing the weighted sum of the objectives at the branch.
#If not, rankedpreferences is stored as a list, and userprefs is a dummy input to adjust this
costfactor=-1.2 #20% percent more important
pagerankfactor=-3
firstbarscore=firstbarcrowd*crowdpref+firstbaralc*alcpref+firstbarcost*costfactor*costpref#+pagerankfactor*firstbarpagerank  #assume higher cost rank means more expensive
secondbarscore=secondbarcrowd*crowdpref+secondbaralc*alcpref+secondbarcost*costfactor*costpref#+pagerankfactor*secondbarpagerank
if(min(firstbarscore,secondbarscore)==firstbarscore){thenextbar=firstbar}else
thenextbar=secondbar
if(thenextbar==nearestbar && Tabu==FALSE){Tabu<<-TRUE;return(pagerankedbar)}else
return(thenextbar)
}
 #Main Function
#starting bar
bartour<-c()
for(i in 1:nobot){
if(i==1){bartour[1]=two_bar_bakeoff(rankedpreferences,startingbar); 
columntoremove=which(names(distances)==bartour[1])
distances=as.data.frame(distances[,-columntoremove])
columntoremove=which(names(distances)==startingbar)
distances=as.data.frame(distances[,-columntoremove])
}
else {
bartour[i]=two_bar_bakeoff(rankedpreferences,bartour[i-1]);
columntoremove=which(names(distances)==bartour[i])
distances=distances[,-columntoremove]
}
}
#Return highest two page ranks that aren't currently in tour.
barsnotintour=setdiff(bars,bartour)
firstpagerank=names(sort(Pagerankvalues[barsnotintour])[length(Pagerankvalues[barsnotintour])])
secondpagerank=names(sort(Pagerankvalues[barsnotintour])[(length(Pagerankvalues[barsnotintour])-1)])
###Get closest bar on our tour to the pageranked bars
distancesdummy=thebars #Keep the upper because we dont have to worry about switching i and j.
names(distancesdummy)=bars
barstokeep=c(bartour,firstpagerank,secondpagerank);barstokeep=barstokeep[!duplicated(barstokeep)]
distances_on_our_tour=as.data.frame(distancesdummy[,c(barstokeep)])
names(distances_on_our_tour)=barstokeep
firstbarid=which(bars==firstpagerank)
secondbarid=which(bars==secondpagerank)
firstbarnearestontour=names(sort(distances_on_our_tour[firstbarid,])[2]) #first is the bar itself
secondbarnearestontour=names(sort(distances_on_our_tour[secondbarid,])[2]) #first is the bar itself
####################################################
#First alternate: run the main program again, but when we get to the nearest bar, choose next the pageranked bar.


#rm(distances2)
#Now that we have the info to solve it
distances2=thebars #Keep the upper because we dont have to worry about switching i and j.
row.names(distances2)=bars
bartouralt<-c()
Tabu=FALSE
for(i in 1:nobot){
if(i==1){bartouralt[1]=two_bar_bakeoff_force(rankedpreferences,startingbar,firstpagerank,firstbarnearestontour,Tabu); 
columntoremove=which(names(distances2)==bartouralt[1])
distances2=as.data.frame(distances2[,-columntoremove])
columntoremove=which(names(distances2)==startingbar)
distances2=as.data.frame(distances2[,-columntoremove])
}
else {
bartouralt[i]=two_bar_bakeoff_force(rankedpreferences,bartouralt[i-1],firstpagerank,firstbarnearestontour,Tabu);
columntoremove=which(names(distances2)==bartouralt[i])
distances2=distances2[,-columntoremove]
}
}

distances2=thebars
row.names(distances2)=bars
 #set the starting bar
bartouralt2<-c()
Tabu=FALSE
for(i in 1:nobot){
if(i==1){bartouralt2[1]=two_bar_bakeoff_force(rankedpreferences,startingbar,secondpagerank,secondbarnearestontour,Tabu) 
columntoremove=which(names(distances2)==bartouralt2[1])
distances2=as.data.frame(distances2[,-columntoremove])
columntoremove=which(names(distances2)==startingbar)
distances2=as.data.frame(distances2[,-columntoremove])
}
else {
bartouralt2[i]=two_bar_bakeoff_force(rankedpreferences,bartouralt2[i-1],secondpagerank,secondbarnearestontour,Tabu)
columntoremove=which(names(distances2)==bartouralt2[i])
distances2=distances2[,-columntoremove]
}
}



bartour=c(startingbar,bartour)
bartouralt=c(startingbar,bartouralt)
bartouralt2=c(startingbar,bartouralt2)
print(bartour)
print("")
print(bartouralt)
print("")
print(bartouralt2)

distscomplete=thebars
row.names(distscomplete)=names(thebars)

dists1<-c();dists2<-c();dists3<-c()
for(count in 1:(nobot)){
dists1[count]=distscomplete[bartour[count],(bartour[count+1])]
dists2[count]=distscomplete[bartouralt[count],(bartouralt[count+1])]
dists3[count]=distscomplete[bartouralt2[count],(bartouralt2[count+1])]
}
#par(mfrow=c(1,3))
png('rplot.png',width=960,height=960)
plot(c(0,dists1),xaxt="n",type="l",xlim=c(.5,(nobot+3)),pch=2,col="red",lwd=2,xlab="",ylab="Distance Travelled (Meters)",main="Distance Traveled on Best Path");
require(calibrate)
textxy(X=seq(1,(nobot+1),1),Y=c(0,dists1),labs=bartour,cex=1.1)
dev.off()
#axis(1,at=1:(nobot+1),labs=bartour,font=2)
#plot(as.ts(dists2),xaxt="n");
#plot(as.ts(dists3),xaxt="n")
sink("bartour.txt")
 cat("The Best Tour for you")
 cat("\n")
 cat(bartour,sep=",  ")
 cat("\n")
  cat("\n")

 cat("The Best Tour with PageRank Considerations")
 cat("\n")
 cat(bartouralt,sep=",  ")
 cat("\n")
  cat("\n")

  cat("The second Best Tour with PageRank Considerations")
   cat("\n")

 cat(bartouralt2,sep=",  ")
sink()




