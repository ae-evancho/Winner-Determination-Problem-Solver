package sample;

import java.util.ArrayList;

public class Bid {
    double offre;
    int bidNb;
    ArrayList<Integer> goodsList = new ArrayList<>();
    int size;

    public Bid(double o, ArrayList<Integer> g, int n, int j ){
        this.offre=o;
        this.goodsList=g;
        this.size=n;
        this.bidNb=j;
    }

    public void printBid(){
        System.out.print(this.offre+ " : ");
        for(int i=0; i<size; i++)
            System.out.print(goodsList.get(i)+" ");
        System.out.println();
    }


}
