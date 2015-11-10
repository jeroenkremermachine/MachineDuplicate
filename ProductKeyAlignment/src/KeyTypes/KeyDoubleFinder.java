package KeyTypes;
import Blocks.Block;
import Models.BlockSet;
import Models.ValueSet;
import Models.Value;
import java.util.*;

public class KeyDoubleFinder
{
    // instance variables - replace the example below with your own

    public KeyDoubleFinder(){
    }
    
    public ArrayList<Double> getDoubles(BlockSet bSet){
       ArrayList<Double> getallenlijst = new ArrayList<Double>();
        String [] splitten= new String[5];
        
                for (Block blo : bSet.getBlockSet()){
                    if (blo.getType().equals("Double")){
                   
                        String ruw = blo.getBlock();
                        ruw=ruw.replace(",", "");
                        if (ruw.charAt(0)=='/') ruw=ruw.replace("/", "");
                        if (ruw.contains(":") || (ruw.contains("-") && (ruw.charAt(0)!='-')) || ruw.contains("/" )){
                         
                            ruw=ruw.replace(":", " ");
                            ruw=ruw.replace("/"," ");
                            ruw=ruw.replace("-", " ");
                            splitten=ruw.split(" ");
                            for (int i = 0; i<splitten.length; i++){
                                
                                getallenlijst.add(Double.parseDouble(splitten[i]));
                                
                            }
                           
                        }
                  
                        else{ 
                            getallenlijst.add(Double.parseDouble(ruw));
                        }
               
                    }
   
                    }
           
                return getallenlijst;
                }

    
        public ArrayList<Double> getUniqueDoubles(ArrayList<Double> DoubleList){
       ArrayList<Double> getallenlijst = new ArrayList<Double>();
       
                for (Double dd : DoubleList){
                 
                    if (!getallenlijst.contains(dd)){
                        getallenlijst.add(dd);
                   
    
                    }
                }
                return getallenlijst;
    }
    public Double getStdvDoubles(ArrayList<Double> DoubleList){
       
                double sum =0;
                double sumDistance=0;
                for (Double dd : DoubleList){
                        sum +=dd;
                    }
                double avg = sum/ DoubleList.size();
                  for (Double dd : DoubleList){
                        sumDistance +=((avg-dd)*(avg-dd));
                    }
                double stdv =Math.sqrt(sumDistance/DoubleList.size()) ;
                  return stdv;
                }
        public ArrayList<String> getStripString(BlockSet bSet){
                ArrayList<String> stripString=new ArrayList<String>();
                
                    for(Block a: bSet.getBlockSet()){
                        if (a.getType().equals("String")){
                        stripString.add(a.getBlock());
                        
                    }
                }
                    return stripString;
                
    
               
    }
            public ArrayList<String> getUniqueStripString(ArrayList<String> a){
                ArrayList<String> stripUniqueString=new ArrayList<String>();
                        if (a.size() > 0){
                                for (String s : a){
                                    
                                    if (!stripUniqueString.contains(s)){ 
                                        stripUniqueString.add(s);}
                                    }
        
                
    
            }
                    
                    return stripUniqueString;
                
    
               
    }
     public int getUniqueValues(ValueSet v){
                ArrayList<String> uniqueValues=new ArrayList<String>();
                        int a=0;
                                for (Value vv : v.getValueSet()){
                                    
                                    if (!uniqueValues.contains(vv.getString())){ a++;
                                        uniqueValues.add(vv.getString());
                                     }
                                    
        
                
    
            }
                    
                    return a;
                
    
               
    }
}

