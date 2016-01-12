/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author Dobre
 */
public class BlockResult {
    String file_name;
    ArrayList<String> max_words;
    HashMap hs;
 
    public BlockResult(){
        max_words = new ArrayList<String>();
        hs = new HashMap<Integer, Integer>();
    }
    
}
