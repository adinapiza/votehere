package cambridge.org.votehere;


import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by cvflores on 10/31/15.
 */
@ParseClassName("Candidate")
public class Candidate  extends ParseObject{


    public String candidateName;
    public int votes;

    public Candidate(){
        super();
    }

    public void retrieve() {
        candidateName = getString("candidateName");
        votes = getInt("votes");
    }


    public static void update(String candidateName){

        ParseQuery<Candidate> query = ParseQuery.getQuery(Candidate.class);
          query.whereEqualTo("cadidateName", candidateName);


        try {
           Candidate candidate =  query.getFirst();

            int votes = candidate.getInt("votes");
            candidate.put("votes",++votes);
            candidate.saveInBackground();

            System.out.println("Saving 0000 votes for " + candidate.get("cadidateName"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        query.findInBackground(new FindCallback<Candidate>() {
//            @Override
//            public void done(List<Candidate> list, ParseException e) {
//                Candidate retPO = list.get(0);
//                //retPO.retrieve();
//                //retPO.setShop(retPO.shop);
//                System.out.println(retPO.getString("candidateName"));
//            }
//        });


    }


    public static List<Candidate> fetchAll (){

        ParseQuery<Candidate> query = ParseQuery.getQuery(Candidate.class);
     //   query.whereEqualTo("Name", filterName);
        List<Candidate> list;
        try {
             list = query.find();
            return list;
        } catch (ParseException e) {
            e.printStackTrace();
        }


//        query.findInBackground(new FindCallback<Candidate>() {
//            @Override
//            public void done(List<Candidate> list, ParseException e) {
//                Candidate retPO = list.get(0);
//                //retPO.retrieve();
//                //retPO.setShop(retPO.shop);
//                System.out.println(retPO.getString("candidateName"));
//            }
//        });

        return null;
    }

}
