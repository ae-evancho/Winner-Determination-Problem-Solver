package sample;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.stream.IntStream;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import javafx.scene.control.TextField;

public class Graph {
    // BitSet[] adjMat;
    int m,n;
    ArrayList<Bid> bidList;
    ArrayList<Integer> cMax;
    double cMaxWeight;
    ArrayList<Integer> cCurrent;
    double cCurrentWeight;
    int debug;
    int [][]adjMatrix;
    int [][]adjRevMatrix;

    public Graph(int g, int b, ArrayList<Bid> bids){
        this.m=g;
        this.n=b;
        this.debug=0;
        // adjMat= new BitSet[n];
        bidList=bids;
        cCurrent=new ArrayList<>();
        cMax=new ArrayList<>();
        cCurrentWeight=0;
        cMaxWeight=0;

        adjMatrix=new int[n][n+1];
        adjRevMatrix=new int[n][n+1];
       /* for (int i = 0; i <n ; i++) {
            adjMat[i]=new BitSet(n);
            for (int j = 0; j <n ; j++) {
                adjMat[i].set(j);
            }
        }*/
    }

    public void createMat(){
        Bid b1,b2;
        int g;
        boolean br;
        int cnt=n*n;
        int [][] aM=new int[n][n];

        for (int i = 0; i < n; i++) {
            b1=bidList.get(i);
            for (int j = i; j < n ; j++) {
                b2=bidList.get(j);
                br=false;
                for (int k = 0; k < b1.size ; k++) {
                    g=b1.goodsList.get(k);
                    if(br) break;
                    for (int l = 0; l < b2.size ; l++) {
                        if(g ==  b2.goodsList.get(l)) {
                            aM[i][j]=1;
                            aM[j][i]=1;
                            //   adjMat[i].set(j,false);
                            //   adjMat[j].set(i,false);
                            cnt=cnt-2;
                            br=true;
                            break;
                        }
                    }
                }
            }
            //   bidList.get(i).goodsList=null;
        }
        System.out.println(cnt);
        for (int i = 0; i <n ; i++)
            for (int j = 0; j <n ; j++)
                if(aM[i][j]==0){ adjMatrix[i][adjMatrix[i][0]+1]=j; adjMatrix[i][0]++;}
                else{ adjRevMatrix[i][adjRevMatrix[i][0]+1]=j; adjRevMatrix[i][0]++;}
    }



    public ArrayList<Integer> colorGraph(ArrayList<Bid> pp){
        ArrayList<Integer> colp= new ArrayList<>();
        ArrayList<TIntList> bI = new ArrayList<>();
        pp.sort(Comparator.comparingInt(ind -> (int)-ind.offre));

        int i=0;
        while(!pp.isEmpty()){

            TIntList temp = new TIntArrayList();
            ArrayList<Bid> u= new ArrayList<>(pp);
            while(!u.isEmpty()){
                Bid b=u.get(0);
                temp.add(b.bidNb-1);
                for (int j=1; j<adjMatrix[b.bidNb-1][0]+1; j++) {
                    u.remove(bidList.get(adjMatrix[b.bidNb-1][j]));
                }
                u.remove(0);
            }
            bI.add(temp);
            for (int j=0; j<bI.get(i).size(); j++) {
                pp.remove(bidList.get(bI.get(i).get(j)));
            }
            i++;
        }
        pp.clear();
        for (int j = 0; j <i ; j++) {
            for (int k=0; k<bI.get(j).size(); k++) {
                pp.add(bidList.get(bI.get(j).get(k)));
                colp.add(j);

            }
        }
        return  colp;
    }


    public void maxClique(ArrayList<Bid> p, ArrayList<Integer> colp, TextField result, TextField sommets){



        if(p.isEmpty() && cCurrentWeight> cMaxWeight) {
            this.cMax = new ArrayList<>(cCurrent);
            String mystr = "";
            for (int i = 0; i < this.cMax.size(); i++) {
                mystr = mystr +this.cMax.get(i)+" ";
            }
            sommets.setText(mystr);
            cMaxWeight=cCurrentWeight;
            result.setText(cMaxWeight+"");
            System.out.println(cMaxWeight);
        }

        while(!p.isEmpty()){

            double upperBound=0;

            int i,j=0, temp;
            i=colp.get(j);
            temp=colp.get(j);
            boolean bool=false;
            upperBound=upperBound+p.get(j).offre;
            while ( j<colp.size()) {
                i = colp.get(j);
                if(i!=temp){ bool=true; temp=i;}
                if(bool){ upperBound=upperBound+p.get(j).offre; bool=false;}
                j++;
            }
            if(cCurrentWeight+upperBound>cMaxWeight){


                Bid v=p.get(0);
                ArrayList<Bid> pSave=new ArrayList<>(p);
                ArrayList<Integer> colpSave= new ArrayList<>(colp);


                cCurrent.add(v.bidNb);debug++;
                cCurrentWeight=cCurrentWeight+v.offre;


                for (j=1; j<adjRevMatrix[v.bidNb-1][0]+1; j++) {
                    p.remove(bidList.get(adjRevMatrix[v.bidNb-1][j]));
                }

                colp=colorGraph(p);

                maxClique(p, colp, result, sommets);


                p=pSave;
                colp=colpSave;
                Integer ii=v.bidNb;
                cCurrent.remove(ii);
                cCurrentWeight=cCurrentWeight-v.offre;

                p.remove(v);
                colp.remove(0);
            }
            else return;
        }
    }




    public void printMat(){
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n ; j++) {
                if(adjMatrix[i][j]==1)  System.out.print(1+" ");
                else System.out.print(0+" ");
            }
            System.out.println();
        }
    }

}
