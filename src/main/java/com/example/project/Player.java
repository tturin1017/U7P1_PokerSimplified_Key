package com.example.project;
import java.util.ArrayList;


public class Player{
    private ArrayList<Card> hand;
    private ArrayList<Card> allCards; //the current community cards + hand
    String[] suits  = Utility.getSuits();
    String[] ranks = Utility.getRanks();
    
    public Player(){
        hand = new ArrayList<>();
    }

    public ArrayList<Card> getHand(){return hand;}
    public ArrayList<Card> getAllCards(){return allCards;}

    public void addCard(Card c){
        hand.add(c);
    }

    public String playHand(ArrayList<Card> communityCards){
        allCards = new ArrayList<Card>();
        for(Card c: communityCards){
            allCards.add(c);
        }
        allCards.add(hand.get(0));
        allCards.add(hand.get(1));
        sortAllCards();

        if(isRoyalFlush()){return "Royal Flush";}
        if(isStraightFlush()){return "Straight Flush";}
        if(isFourOfAKind()){return "Four of a Kind";}
        if(isFullHouse()){return "Full House";}
        if(isFlush()){return "Flush";}
        if(isStraight()){return "Straight";}
        if(isThreeOfAKind()){return "Three of a Kind";}
        if(isTwoPair()){return "Two Pair";}
        if(isPair()){return "A Pair";}
        if(isHighCard()){return "High Card";}        
        return "Nothing";
    }

    public void sortAllCards(){ 
        //use selection sort
        for(int i=0; i<allCards.size(); i++){
            int min_i = i;
            for(int j=i+1;j<allCards.size();j++){
                if(allCards.get(min_i).getRankValue()>allCards.get(j).getRankValue()){
                    min_i = j;
                }
                //swap
                Card temp = allCards.get(i);
                allCards.set(i,allCards.get(min_i));
                allCards.set(min_i,temp);
            }
        }
    }

    public ArrayList<Integer> findRankingFrequency(){
        ArrayList<Integer> freqList = new ArrayList<>();
        for(String s: ranks){ //loop through ranks {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
            int count = 0;
            for(Card card: allCards){ //loop through community + hand cards
                if(card.getRank().equals(s)){
                    count++;
                }
            }
            freqList.add(count);
        }
        return freqList; 
    }

    public ArrayList<Integer> findSuitFrequency(){
        ArrayList<Integer> freqList = new ArrayList<>();
        for(String s: suits){ //loop through {"Hearts","Diamonds","Clubs", "Spades"};
            int count = 0;
            for(Card c: allCards){
                if(c.getSuit().equals(s)){
                    count++;
                }
            }
            freqList.add(count);
        }
        return freqList; 
    }

    public boolean isRoyalFlush(){
        //has to be a flush, check
        if(isFlush()){ //hand + community cards are all of the same suit
            //now check for straight
            if(isStraight()){
                //now check if lowest card is 10
                //loop through community cards and find min
                int min = Integer.MAX_VALUE;
                for(Card c: allCards){
                    if(c.getRankValue()<min){
                        min = c.getRankValue();
                    }
                }
                if(min==10){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean isStraightFlush(){
        if(isStraight() && isFlush()){
            return true;
        }

        return false;
    }

    public boolean isFourOfAKind(){
        //cheak for a frequency of 4
        ArrayList<Integer>freqList = findRankingFrequency();
        for(int i=0;i<freqList.size();i++){
            if(freqList.get(i)==4){
                return true;
            }
        }
        return false;
    }

    public boolean isFullHouse(){ //3 pair and a two pair .. can't reuse 3 of a kind and pair because what if 3 pair is the community cards a
        ArrayList<Integer>freqList = findRankingFrequency();
        boolean hasTwoPair = false;
        boolean hasThreePair = false;
        for(int num : freqList){
            if(num == 3){
                hasThreePair = true;
            }
            if(num == 2){
                hasTwoPair = true;
            }
        }

        if(hasTwoPair && hasThreePair){
            return true;
        }else{
            return false;
        }
    }

    public boolean isFlush(){
        ArrayList<Integer> suitFreqList = findSuitFrequency();
        for(int i=0;i<suitFreqList.size(); i++){
            if(suitFreqList.get(i)==5){
                return true;
            }
        }
        return false;
    }

    public boolean isStraight(){
        for(int i =0; i<allCards.size()-1;i++){
            if(allCards.get(i+1).getRankValue()-allCards.get(i).getRankValue()!=1){ //the current rank value should be 1 less than i+1 value
                return false;
            }
        }
        return true;
    }

    public boolean isThreeOfAKind(){
        ArrayList<Integer>freqList = findRankingFrequency();
        System.out.println(freqList);
        for(int i=0 ; i<freqList.size(); i++){
            if(freqList.get(i)==3){
                if(hand.get(0).getRank().equals(ranks[i]) || hand.get(1).getRank().equals(ranks[i])){
                    //compare hand rank with freqList rank. If at least one of them is the same, its a 3 pair
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTwoPair(){
        ArrayList<Integer> freqList = findRankingFrequency();
        boolean firstPair = false;
        boolean secondPair = false;
        String c1 = hand.get(0).getRank();
        String c2 = hand.get(1).getRank();
        if(isPair()){ // one of the pairs is in the hand
            for(int i=0;i<freqList.size();i++){ //check if there is a pair in the community cards
                if(freqList.get(i)==2){
                    if(ranks[i]!=c1 && ranks[i]!=c2){
                        return true;
                    }
                }
            }
        }else{ //if the hand isn't a pair, each card in hand must have a pair in the community cards
            for(int i=0;i<freqList.size();i++){ 
                if(freqList.get(i)==2){
                    if(ranks[i]==c1){
                        firstPair = true;
                    }else if (ranks[i]==c2){
                        secondPair = true;
                    }
                }
            }
            if(firstPair && secondPair){
                return true;
            }else{
                return false;
            }
         
        }
        return false;
    }

    public boolean isPair(){
        ArrayList<Integer> rankingFreqList = findRankingFrequency();
        for(int i=0; i< rankingFreqList.size();i++){
            //if there is a pair and that pair is a part of the players hand, not just in the community deck
            if(rankingFreqList.get(i)==2){
                if(hand.get(0).getRank().equals(ranks[i])||hand.get(1).getRank().equals(ranks[i])){
                    return true;
                }
            } 
        }
        return false;
    }
    
    public boolean isHighCard(){
        int max = Integer.MIN_VALUE;
        for(int i =0 ; i < allCards.size();i++){
            if(allCards.get(i).getRankValue()>max){
                max = allCards.get(i).getRankValue();
            }
        }

        if(max == hand.get(0).getRankValue() || max==hand.get(1).getRankValue()){
            return true;
        }
        
        return false;
    }
    
    @Override
    public String toString(){
        return hand.toString();
    }




}
