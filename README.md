# HTTP Load Tester CLI

### Idea for the project came from John Crickett's Coding challenges.
https://codingchallenges.fyi/

This command-line interface (CLI) tool is designed to test the load of specified HTTP endpoints. 

You can parametrize 
- the URL,
- request details (headers, method, body), 
- specify the number of URLs from a file, 
- the number of requests to make, 
- and the number of concurrent threads to use.

The output consists of useful metrics in a following format: 
```Request URL: http://localhost:8080/endpoint3
Total Requests (2XX): 100
Failed Requests (5XX): 0
Request/Second: 22.00

Total Request Time (s) (Min, Max, Mean): 0.02, 0.05, 0.04
Time To First Byte (s) (Min, Max, Mean): 0.02, 0.05, 0.04
Time to Last Byte (s) (Min, Max, Mean): 0.02, 0.05, 0.04
```

## Input Parameters

- `-u, --url`
    - **Description:** A valid URL for the HTTP endpoint.
    - **Example:** `-u http://example.com/api`

- `-m, --method`
    - **Description:** The HTTP method to be used.
    - **Example:** `-m GET`

- `-H`
    - **Description:** HTTP headers for the request.
    - **Example:** `-H "Content-Type: application/json"`
    - **Can be specified multiple times identical to specifying headers in curl**

- `-d`
    - **Description:** The HTTP request body.
    - **Example:** `-d '{"key": "value"}'`

- `-n`
    - **Description:** The number of requests to make.
    - **Example:** `-n 100`

- `-c`
    - **Description:** The number of concurrent threads to use.
    - **Example:** `-c 10`

- `-f`
    - **Description:** A file containing URLs to test.
    - **Example:** `-f urls.txt`

## Example Use

To run the project you need java with maven installed. Clone the repo and run
````bash
mvn clean install
````
which will produce a jar with dependencies which can be executed with a following command

```bash
java -jar ccload-jar-with-dependencies.jar -u http://localhost:8080/endpoint3 -c 1 -n 100 -m POST -d body -H "Content-Type: text/plain"
```