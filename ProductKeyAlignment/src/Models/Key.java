package Models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Key
{
    private String name;
    private ArrayList<Value> values;
    private String type;
    private String UnitMeasure;
    private ArrayList<String> stripString; // allStringValues alle string blocks
    private ArrayList<String> stripUniqueString; // k.StringValues all unique String Blocks
    private int nrStringValues; // nrStringValues aantal string blocks
    private ArrayList<Double> splitList; //allDouble VAlues
    private ArrayList<Double> uniqueSplitList; // k.DoubleValues
    private int diversity;
    private double coverage;
    private double stdv; //k.stdv
    
    
    
    public Key(String keyName, String valueName)
    {
        name = keyName;
        values = new ArrayList<Value>();
        values.add(new Value(valueName));
    }

    public String getName()
    {
        return name;
    }
    public void addValue(String value){

        values.add(new Value(value));
    }
    public ArrayList<Value> getValues()
    {
        return values;
    }
    
    public void setType(String s){
        type = s;
    }
    
    public String getType(){
        return type;
    }
    public ArrayList<Double> getsplitList()
    {
        return splitList;
    }
    public void addsplitList(ArrayList<Double> splitlist)
    {
        splitList=splitlist;
    }
        public ArrayList<Double> getUniquesplitList()
    {
        return uniqueSplitList;
    }
    public void addUniqueSplitList(ArrayList<Double> splitlist)
    {
        uniqueSplitList=splitlist;
    }
            public Double getstdv()
    {
        return stdv;
    }
    public void addstdv(Double s)
    {
        stdv=s;
    }
    // stripStrings
            public ArrayList<String> getStripString()
    {
        return stripString;
    }
    public void addStripString(ArrayList<String> a)
    {
        stripString=a;
        nrStringValues=a.size();
        

    
            }
        
        public void addUniqueStripString(ArrayList<String> a)
    {
        stripUniqueString=a;
 

        }
     public ArrayList<String> getUniqueStripString()
    {
        return stripUniqueString;
    }
      public void addDiversity(int a)
    {
        diversity=a;
 

        }
     public int getDiversity()
    {
        return diversity;
    }
          public void addCoverage(Double a)
    {
        coverage=a;
 

        }
     public Double getCoverage()
    {
        return coverage;
    }
     
     public void setUnitMeasure(String UM){
    	 UnitMeasure = UM;
     }
     
     public String getUnitMeasure(){
    	 return UnitMeasure;
     }
}

