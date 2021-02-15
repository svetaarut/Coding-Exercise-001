#Coding Exercise
This is a coding exercise for Statistics, Buckets and Soundex
Sample data is contained within a csv file with 3 string columns (firstName, lastName, email) and 3 integer columns (age, vantageScore and ficoScore)

##API and Usage
Run as a Spring Boot application

###Get Statistics
Returns the statistics for all the numeric columns from the csv file. The statistics includes min, max, count, distinct count, mean and standard deviation
Request: http://localhost:8080/api/v1/statistics
Sample Response:
{
	"statistics": {
		"numericDistributions": [
		{
			"columnName": "age",
			"count": "963",
			"distinctCount": "567",
			"min": "7",
			"max": "89",
			"mean": "48.65356425325",
			"stddev": "67.86576457987"
		},
		{
			"columnName": "ficoScore",
			"count": "963",
			"distinctCount": "90",
			"min": "17",
			"max": "897",
			"mean": "194.0052929821284",
			"stddev": "112.62990893280038"
		},
		{
			"columnName": "vantageScore",
			"count": "963",
			"distinctCount": "59",
			"min": "117",
			"max": "845",
			"mean": "193.005294575621284",
			"stddev": "212.656856868280038"
		}
		]
	}
}

###Get Bucket Data
Implements Bucketizer by transforming a column of continuous features to a column of feature buckets, where the buckets are specified by users. 
For this endpoint we will default the buckets to 5. 
This endpoint returns the bucketized data only for numeric columns which are passed as a query parameter in the URL. Non-numeric columns shall be considered as bad request.

Request: http://localhost:8080/api/v1/bucketdata?columnName={columnName} where columnName is one of the numeric columns provided above 
	e.g. http://localhost:8080/api/v1/bucketdata?columnName=age
Sample Response:	
{
	"columnName": "age",
	"bucketData": [
		{
		"bucketNumber": 1,
		"range": "1 - 21",
		"count": "50"
		},
		{
		"bucketNumber": 2,
		"range": "21 - 41",
		"count": "37"
		},
		{
		"bucketNumber": 3,
		"range": "41 - 61",
		"count": "6"
		},
		{
		"bucketNumber": 4,
		"range": "61 - 81",
		"count": "3"
		},
		{
		"bucketNumber": 5,
		"range": "81 - 101",
		"count": "7"
		}
	]
}

Exceptions are provided if the columnName contains non-numeric data or does not exist
{
	"errorCode": 400,
	"ErrorDesc": "Bad Request: Only Numeric Columns are allowed: (age, vantageScore, ficoScore)."
}

###Get Match
The Soundex code for a name consists of a letter followed by three numerical digits: the letter is the first letter of the name, and the digits encode the remaining consonants
Refer to https://en.wikipedia.org/wiki/Soundex for further description 
Given any string column value as a query parameter, return all the column values which fall within plus or minus 100 range for Soundex codes.
e.g. if the query is for Robert, whose Soundex code is R163, any names that have R with values between and including 63 and 263 and returned.

Request: http://localhost:8080/api/v1/match?columnName={targetColumnName}&name={targetName} where targetColumnName is one of the string columns provided above 
	e.g. http://localhost:8080/app/v1/match?columnName=firstName&name=robert
Sample Response:
{
	“output”: [“rupert”, “rubin”]
}

Exceptions are provided if the columnName contains numeric data or does not exist
{
	"errorCode": 400,
	"ErrorDesc": "Bad Request: Only String Columns are allowed: (firstName, lastName, email)"
}
Exceptions are provided if no records are found
{
	"errorCode": 404,
	"ErrorDesc": "No matches found."
}

##Installation
input data location is specified in application.yml (for the testing purposes it's added to the resources folder)

###Requirements
Implemented in Java 8 and tested under Windows 10

##Testing
Unit/Integration tests can be run using the maven command: mvn test
For application testing please use swagger page  http://localhost:8080/swagger-ui.html

##Assumptions / Implementation Notes
###Input File
- No bad data in input file 
	- No strings with internal commas
	- No negative or non-integer values in numeric columns
	- No non-numeric values in numeric columns
	- No nulls
	
- Input file is a fixed format with headers
	- No additional columns
	- Column order matters

###Get Statistics
- Implementation uses Google Guava package (Stats class)

###Get Bucket Data
- There are enough numbers provided in the input to create the requested buckets.
  For example, if 5 buckets are requested, all the numbers should not be just 1 or 2. 
- Bucket Range Labels A - B are to be read as Number >= A and Number < B
  e.g. Bucket Range 1 - 21 means a number is included if 1 Number >= 1 and Number < 21
- Bucket Ranges are created by ([maximum number] - [minimum number] + 1]) DIV [# of Buckets].
- Bucket Start and Endpoints are created by starting with the minimum number and adding the Bucket Range
  The next bucket starts with the previous Endpoint. 
  For the last bucket, the Endpoint is set to the [Maximum Number] + 1. This means that 
  all the bucket labels will have the same range, but that the last bucket endpoint will
  have a number not in the set of data.
	
###Get Match 
- Implementation uses the Apache Commons Codec package (Soundex Class) for Soundex.
- The Soundex match response should only include distinct values
  
##Authors
S. Aroutiouniants

##Future enhancements
General:	Allow users to have dynamic sets of column data 
Bucketizer: Allow users to provide number of buckets
Soundex:	Allow users to provide multiple attributes (e.g. firstName=Dana&lastName=Smythe)

