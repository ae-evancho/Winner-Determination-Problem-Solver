package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Scanner;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TDoubleIntMap;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TDoubleIntHashMap;
import javafx.scene.control.TextField;


public class EcommMain implements Runnable {

    public static TextField result;
    public static TextField time;
    public static TextField sommets;
    public static String p;
    public static boolean off;
    long startTime;
    private volatile boolean exit = false;

    public EcommMain(TextField r, TextField t, String p, TextField sommets){
        this.p=p;
        this.result=r;
        this.time=t;
        this.sommets = sommets;

    }

    public void run() {

       while(!exit){
        //Traitement ////////////////////////////////////



         startTime = System.currentTimeMillis();

           System.out.println( startTime);
        /*
         * Future Changes :
         * Add a time limit
         * */

        String path = this.p;
        Scanner reader = null;
        try {
            reader = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        off = true;
        ArrayList<Bid> bidList= new ArrayList<>();
        ArrayList<Integer> temp;
        double d;
        String [] parser;

        parser=reader.nextLine().split(" ");
        int nbGoods=Integer.parseInt((parser[0]));
        int nbBids=Integer.parseInt((parser[1]));
        int j=1;
        while(reader.hasNextLine()){
            parser=reader.nextLine().split(" ");
            temp =new ArrayList<>();
            for(int i=1;i<parser.length;i++)
                temp.add(Integer.parseInt(parser[i]));
            d=Double.parseDouble(parser[0]);
            Bid b= new Bid(d, temp, temp.size(), j);
            bidList.add(b);
            j++;
        }
        reader.close();

/*
        HashMap<Double, Integer> bidMap=new HashMap<>();
        for(int i=0;i<bidList.size();i++) {
            if (!bidMap.containsKey(bidList.get(i).offre))
                bidMap.put(bidList.get(i).offre, bidList.get(i).bidNb);
            else{
                System.out.println("noooooo");
                break;
            }
        }



        TDoubleList offList= new TDoubleArrayList();
        for(int i=0;i<bidList.size();i++)
            offList.add(bidList.get(i).offre);

*/
        //Algorithme ///////////////////////////////



        Graph myGrph=new Graph(nbGoods, nbBids, bidList);
        myGrph.createMat();
        // myGrph.printMat();
        ArrayList<Bid> rtp= new ArrayList<Bid>(bidList);
        ArrayList<Integer> rtcolp=myGrph.colorGraph(rtp);

        for (int i = 0; i <rtp.size() ; i++) {
            System.out.print(rtp.get(i).bidNb+" ");
        }
        System.out.println();
        for (int i = 0; i <rtcolp.size() ; i++) {
            System.out.print(rtcolp.get(i)+" ");
        }

        myGrph.maxClique(rtp, rtcolp, result, sommets);
        off = false;
        System.out.println();

        for (int i = 0; i < myGrph.cMax.size(); i++) {
            System.out.print(myGrph.cMax.get(i)+" ");
        }
        System.out.println();

        System.out.println(String.format("%.2f", myGrph.cMaxWeight));



        long endTime = System.currentTimeMillis();

        System.out.println("That took " + (endTime - startTime) + " milliseconds");

        System.out.println("steps :"+ myGrph.debug);

        time.setText((endTime - startTime) + " ms");
        result.setText(String.format("%.2f", myGrph.cMaxWeight));

/*
        TIntList list = new TIntArrayList();
        list.add( 1 );
        list.add( 2 );
        list.add( 3 );
        for (int i = 0; i < list.size() ; i++) {
            System.out.println(list.get(i));
        }
*/
           this.stop();
       }

        System.out.println("done");

    }

    public void stop(){
        exit = true;
        long endTime = System.currentTimeMillis();
        time.setText((endTime - startTime) + " ms");
        off = false;
    }
}
