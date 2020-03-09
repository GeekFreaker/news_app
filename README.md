        I used this as my guide when creating this app.
    
         A  Summary of the Guardians API there's a few nifty methods
         I will use. It has five end points namely:

         We provide several endpoints to retrieve different items:

         Content
         Tags
         Sections
         Editions
         Single item

         ### Content(q)
    ---
         The content endpoint (/search) returns all pieces of content in the API.

         ##Query operators
    --
         The q parameter supports AND, OR and NOT operators.

         ##Filters operators
    --     
         Some filters support AND, OR and NOT operators through a a specific syntax:

         AND: ,
         OR: |
         NOT: -
         Expressions can be grouped using ().

         ### Phrase search
    
         You can also use double quotes to search for exact phrases.

         ## Tags
    ---
         The tags endpoint (/tags) returns all tags in the API.
         All Guardian content is manually categorised using these tags,
         of which there are more than 50,000.

         ### Editions
    ---
         Editions are the different front main pages of the Guardian site we have.
         At current we have editions for the United Kingdom, the United States and Australia.

         ### Sections
    ---     
         The sections endpoint(/sections) returns all sections in the API.
         We use sections to logically group our content.

         ### Single item
    ---     
         The single item endpoint returns all the data we have for a given single item id.


