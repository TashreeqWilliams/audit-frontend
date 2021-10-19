package za.ac.cput.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import za.ac.cput.entity.*;

import java.io.IOException;
import java.util.ArrayList;

public class Client {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static OkHttpClient client = new OkHttpClient();

    //READ
    private String run(String URL) throws IOException {
        Request request = new Request.Builder()
                .url(URL)
                .build();
        try(Response response = client.newCall(request).execute()){
            return response.body().string();
        }
    }

    //CREATE
    private String post(final String URL, String json) throws IOException{
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();
        try(Response response = client.newCall(request).execute()){
            return response.body().string();
        }
    }

    //UPDATE
    private String put(final String URL, String json) throws IOException{
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(URL)
                .put(body)
                .build();
        try(Response response = client.newCall(request).execute()){
            return response.body().string();
        }
    }

    public UserAccount login(String email, String password){
        try{
            final String URl = "http://localhost:8080/useraccount/login/" + email + "/" + password;
            String responseBody = run(URl);

            Gson g = new Gson();
            JsonArray userAccounts = (JsonArray) JsonParser.parseString(responseBody);
            if(userAccounts.size() == 1){
                UserAccount userAccount = g.fromJson(userAccounts.get(0), UserAccount.class);
                return userAccount;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public Auditor getAuditor(String id){
        try{
            final String URl = "http://localhost:8080/auditor/read/" + id;
            String responseBody = run(URl);

            Gson g = new Gson();
            JsonArray auditors = (JsonArray) JsonParser.parseString(responseBody);
            if(auditors.size() == 1){
                Auditor auditor = g.fromJson(auditors.get(0), Auditor.class);
                return auditor;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Auditor updateAuditor(Auditor auditor){
        try{
            final String URl = "http://localhost:8080/auditor/update";

            Gson g = new Gson();
            String jsonString = g.toJson(auditor);
            String x = put(URl, jsonString);
            if(x != null)
                return auditor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Auditor createAuditor(Auditor auditor){
        try{
            final String URl = "http://localhost:8080/auditor/create";

            Gson g = new Gson();
            String jsonString = g.toJson(auditor);
            String x = post(URl, jsonString);
            if(x != null)
                return auditor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public UniversityStaff getUser(String id){
        try{
            final String URl = "http://localhost:8080/universitystaff/read/" + id;
            String responseBody = run(URl);

            Gson g = new Gson();
            JsonArray users = (JsonArray) JsonParser.parseString(responseBody);
            if(users.size() == 1){
                UniversityStaff user = g.fromJson(users.get(0), UniversityStaff.class);
                return user;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UniversityStaff updateUser(UniversityStaff universityStaff){
        try{
            final String URl = "http://localhost:8080/universityStaff/update";

            Gson g = new Gson();
            String jsonString = g.toJson(universityStaff);
            String x = put(URl, jsonString);
            if(x != null)
                return universityStaff;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public UniversityStaff createUser(UniversityStaff universityStaff){
        try{
            final String URl = "http://localhost:8080/universityStaff/create";

            Gson g = new Gson();
            String jsonString = g.toJson(universityStaff);
            String x = post(URl, jsonString);
            if(x != null)
                return universityStaff;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Issue getIssue(String id){
        try{
            final String URl = "http://localhost:8080/issue/read/" + id;
            String responseBody = run(URl);

            Gson g = new Gson();
            JsonArray issues = (JsonArray) JsonParser.parseString(responseBody);
            if(issues.size() == 1){
                Issue issue = g.fromJson(issues.get(0), Issue.class);
                return issue;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Issue updateIssue(Issue issue){
        try{
            final String URl = "http://localhost:8080/issue/update";

            Gson g = new Gson();
            String jsonString = g.toJson(issue);
            String x = put(URl, jsonString);
            if(x != null)
                return issue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Issue createIssue(Issue issue){
        try{
            final String URl = "http://localhost:8080/issue/create";

            Gson g = new Gson();
            String jsonString = g.toJson(issue);
            String x = post(URl, jsonString);
            if(x != null)
                return issue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Report getReport(String id){
        try{
            final String URl = "http://localhost:8080/report/read/" + id;
            String responseBody = run(URl);

            Gson g = new Gson();
            JsonArray reports = (JsonArray) JsonParser.parseString(responseBody);
            if(reports.size() == 1){
                Report user = g.fromJson(reports.get(0), Report.class);
                return user;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Report updateReport(Report report){
        try{
            final String URl = "http://localhost:8080/report/update";

            Gson g = new Gson();
            String jsonString = g.toJson(report);
            String x = put(URl, jsonString);
            if(x != null)
                return report;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Report createReport(Report report){
        try{
            final String URl = "http://localhost:8080/report/create";

            Gson g = new Gson();
            String jsonString = g.toJson(report);
            String x = post(URl, jsonString);
            if(x != null)
                return report;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Ticket getTicket(String id){
        try{
            final String URl = "http://localhost:8080/ticket/read/" + id;
            String responseBody = run(URl);

            Gson g = new Gson();
            JsonArray issues = (JsonArray) JsonParser.parseString(responseBody);
            if(issues.size() == 1){
                Ticket ticket = g.fromJson(issues.get(0), Ticket.class);
                return ticket;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Ticket updateTicket(Ticket ticket){
        try{
            final String URl = "http://localhost:8080/ticket/update";

            Gson g = new Gson();
            String jsonString = g.toJson(ticket);
            String x = put(URl, jsonString);
            if(x != null)
                return ticket;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Ticket createTicket(Ticket ticket){
        try{
            final String URl = "http://localhost:8080/ticket/create";

            Gson g = new Gson();
            String jsonString = g.toJson(ticket);
            String x = post(URl, jsonString);
            if(x != null)
                return ticket;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public ArrayList<Issue> getAllIssues(){
        try{
            final String URl = "http://localhost:8080/issue/getall";
            String responseBody = run(URl);
            ArrayList<Issue> allIssues = new ArrayList<>();
            Gson g = new Gson();
            JsonArray issues = (JsonArray) JsonParser.parseString(responseBody);
            for(int i = 0; i < issues.size(); i++)
                allIssues.add(g.fromJson(issues.get(i), Issue.class));
            return allIssues;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Ticket> getAllTickets(){
        try{
            final String URl = "http://localhost:8080/ticket/getall";
            String responseBody = run(URl);
            ArrayList<Ticket> allTickets = new ArrayList<>();
            Gson g = new Gson();
            JsonArray tickets = (JsonArray) JsonParser.parseString(responseBody);
            for(int i = 0; i < tickets.size(); i++)
                allTickets.add(g.fromJson(tickets.get(i), Ticket.class));
            return allTickets;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) throws IOException {
        //new Client().login("test", "testiing");
       // System.out.println(run("http://localhost:8080/issue/getall"));
    }
}