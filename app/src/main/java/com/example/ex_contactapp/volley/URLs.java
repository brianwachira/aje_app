package com.example.ex_contactapp.volley;

public class URLs {
    // //getAllContactGroups getAllGroupLists getAllMessages
    private static final String ROOT_URL = "https://aje-api.000webhostapp.com/endpoints.php?apicall=";
    public static final String URL_LOGIN= ROOT_URL + "login"; //'userId', 'givenName'
    public static final String URL_CREATECONTACTGROUP = ROOT_URL + "createcontactgroup"; //'id','groupname','numofcontacts','userid'
    public static final String URL_CREATEGROUPLIST = ROOT_URL + "creategrouplist"; //'contactid','firstname','lastname','phonenumber','groupid','middlename'
    public static final String URL_INSERTMESSAGE = ROOT_URL + "insertmessage"; //'messageid','messagecontent','groupid'

    public static final String URL_GETALLCONTACTGROUPS =ROOT_URL + "getAllContactGroups";
    public static final String URL_GETALLGROUPLISTS = ROOT_URL + "getAllGroupLists";
    public static final String URL_GETALLMESSAGES = ROOT_URL + "getAllMessages";

}
