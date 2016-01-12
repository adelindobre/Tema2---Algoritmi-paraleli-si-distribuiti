/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema2;

import java.util.*;
import java.util.concurrent.Callable;


/**
 *
 * @author Dobre
 */
public class Reduce implements Callable<FinalResult>{
    String file_name;
    ArrayList<BlockResult> array;
    HashMap final_hs;
    ArrayList<String> max_words;
    int max_length;
    float rang;
    
    public Reduce(String file_name){
        this.file_name = file_name;
        array = new ArrayList<BlockResult>();
        final_hs = new HashMap<Integer, Integer>();
        max_words = new ArrayList<String>();
        max_length = 0;
    }
    public void addElement(BlockResult block){
        array.add(block);
    }
    public void combine(){
        Iterator it;
        Set<Integer> st;
        int lungime, nr_apar;
        for(int i = 0; i < array.size(); i++){
            st = array.get(i).hs.keySet();
            it = st.iterator();
            while(it.hasNext()){
                lungime = (int)it.next();
                if(!final_hs.containsKey(lungime)){
                    final_hs.put(lungime, array.get(i).hs.get(lungime));
                } else {
                    nr_apar = (int)final_hs.get(lungime) + (int)array.get(i).hs.get(lungime);
                    final_hs.replace(lungime, final_hs.get(lungime), nr_apar);
                }
            }
            for(int j = 0; j < array.get(i).max_words.size(); j++){
                if(array.get(i).max_words.get(j).length() > max_length){
                    max_length = array.get(i).max_words.get(j).length();
                    max_words.clear();
                    max_words.add(array.get(i).max_words.get(j));
                } else if(array.get(i).max_words.get(j).length() == max_length){
                    max_words.add(array.get(i).max_words.get(j));
                }
            }
        }
    }
    public float findFib(int position){
        float fib_number, first = 0, second = 1, sum = 0;
        
        for(int i = 2; i <= position; i++){
            sum = first + second;
            first = second;
            second = sum;
        }
        return sum;
    }
    public void process(){
       float nr_total_words = 0, sum = 0, nr_apar;
       int length;
       Set<Integer> key = final_hs.keySet();
       Iterator it = key.iterator();
       while(it.hasNext()){
            length = (int)it.next();
            nr_apar = (int)final_hs.get(length);
            sum += findFib(length + 1) * nr_apar;
            nr_total_words += nr_apar;
       }
       rang = sum / nr_total_words;
    }
    @Override
    public FinalResult call() throws Exception {
        int count = 1;
        combine();
        process();
        FinalResult fr = new FinalResult(file_name, rang);
        Collections.sort(max_words, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    String p1 = (String)o1;
                    String p2 = (String)o2;
                    return p1.compareTo(p2);
                }
            });
         for(int i = 1; i < max_words.size(); i++){
             if(max_words.get(i).compareTo(max_words.get(i-1)) != 0){
                 count++;
             }
         }
        fr.max_dim = max_length; 
        fr.max_words_nr = count;
        return fr;
    }
}
