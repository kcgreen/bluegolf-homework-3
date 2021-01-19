# bluegolf-homework-3

## Question 3

Many golf organizations use volunteers to assist with live scoring. In order to speed up the process of logging these volunteers in, design a process that allows the organization to generate an email that will be sent to the volunteers with a link to log in.  The link should include a secure token that will identify the user and the tournament and, as long as the token has not expired, log them in so that they are able to start scoring.

Build a class that has two primary functions:
-	Given a user and a tournament, generate an encrypted token which expires in 24 hours.
-	Given a token
    -	Validate that it has not yet expired
    -	If it is still valid, return the user and tournament.
    
### Building the Token

This solution uses JWTS to build and parse the token.  The strength of the token encryption is based on length and complexity of the secret.  This restful service contains endpoints to set and test the encrypted token.

Setting the token requires two request body parameters in JSON and returns a JSON response containing the token.

```
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X POST --data '{"userName": "LUKE","tournamentName": "Jedi Master Tournament"}'  http://localhost:8080/token/v1/set


{"token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJMVUtFIiwiZXhwIjoxNjExMDkxNDEzLCJWb2x1bnRlZXIiOiJ7XG4gIFwidXNlck5hbWVcIiA6IFwiTFVLRVwiLFxuICBcInRvdXJuYW1lbnROYW1lXCIgOiBcIkplZGkgTWFzdGVyIFRvdXJuYW1lbnRcIlxufSJ9.p2hL0kE4njYym6UQhA0oP81gOqWVNVE5cxHVYlQxYFmCHbaxJEL777TF0KStVdpxLLGZ7FZ1l7mWhsV955zMWw"}
```
If the user name or the tournament name do not pass validation, or there is an error building the token, then a null token is returned in the response.

```
url -i -H "Accept: application/json" -H "Content-Type: application/json" -X POST --data '{"userName": "PALPATINE","tournamentName": "Jedi Master Tournament"}'  http://localhost:8080/token/v1/set

{"token":null}
```

### Parsing the Token

Again, this solution uses JWTS to parse the token and responds with the original user name and tournament name along with an attribute indication to allow (allow: true) or deny (allow: false) the user authentication to the resource.

The token is presented as a request parameter in the query string.  The response is JSON although future implementation would be within a login filter chain.

```
curl -v http://localhost:8080/token/v1/test/?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJMVUtFIiwiZXhwIjoxNjExMDkxNDEzLCJWb2x1bnRlZXIiOiJ7XG4gIFwidXNlck5hbWVcIiA6IFwiTFVLRVwiLFxuICBcInRvdXJuYW1lbnROYW1lXCIgOiBcIkplZGkgTWFzdGVyIFRvdXJuYW1lbnRcIlxufSJ9.p2hL0kE4njYym6UQhA0oP81gOqWVNVE5cxHVYlQxYFmCHbaxJEL777TF0KStVdpxLLGZ7FZ1l7mWhsV955zMWw | json_pp

{
   "tokenRequest" : {
      "tournamentName" : "Jedi Master Tournament",
      "userName" : "LUKE"
   },
   "allow" : true
}
```

A user who was originally granted a token, but subsequently invalidated will receive a deny (allow: false) response.

```
curl -v http://localhost:8080/token/v1/test/?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBTkFLSU4iLCJleHAiOjE2MTExNTQ4MzgsIlZvbHVudGVlciI6IntcbiAgXCJ1c2VyTmFtZVwiIDogXCJBTkFLSU5cIixcbiAgXCJ0b3VybmFtZW50TmFtZVwiIDogXCJKZWRpIE1hc3RlciBUb3VybmFtZW50XCJcbn0ifQ.O_gRwjpgGUpQxlI00PtdkAd1Wk1xwl9uCt37snefbR-NCTZmED3j7HfVapKNV0DNQra2RdU-ElM2SCxWfPJEvg | json_pp

{
   "tokenRequest" : {
      "tournamentName" : "Jedi Master Tournament",
      "userName" : "ANAKIN"
   },
   "allow" : false
}
```

Any token which expires or has indication of tampering will not be parsed and will receive a deny (allow: false) response.

```
curl -v http://localhost:8080/token/v1/test/?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJMVUtFIiwiZXhwIjoxNjExMDkxNDEzLCJWb2x1bnRlZXIiOiJ7XG4gIFwidXNlck5hbWVcIiA6IFwiTFVLRVwiLFxuICBcInRvdXJuYW1lbnROYW1lXCIgOiBcIkplZGkgTWFzdGVyIFRvdXJuYW1lbnRcIlxufSJ9.p2hL0kE4njYym6UQhA0oP81gOqWVNVE5cxHVYlQxYFmCHbaxJEL777TF0KS | json_pp

{
   "tokenRequest" : null,
   "allow" : false
}
```
