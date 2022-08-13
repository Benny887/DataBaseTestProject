//package json;
//
//import TableClasses.Search;
//import com.google.gson.*;
//import java.io.*;
//
//
//public class JavaObjectMaker {
//    public Search makePOJOfromJSON(File file) throws IOException {
//        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()
//                .create();
//        Search dataTosearch = null;
//        try(FileReader fr = new FileReader(file)){
//                dataTosearch = gson.fromJson(fr,Search.class);
//            }
//        return dataTosearch;
//    }
//
//}
