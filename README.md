# **Dropbox-Equivalent Service**


## **Overview**

This project is a simplified version of Dropbox, allowing users to upload, retrieve, delete, and list files using a RESTful API. The service is built using Spring Boot, PostgreSQL, and AWS S3 for file storage.


## **Features**



* **File Upload**: Upload files to AWS S3.
* **File Retrieval**: Download files from AWS S3.
* **File Deletion**: Delete files from AWS S3.
* **List Files**: List all files in a specific S3 bucket.


## **Tech Stack**



* **Backend**: Spring Boot
* **Database**: PostgreSQL (hosted on AWS RDS)
* **File Storage**: AWS S3


## **Prerequisites**



* Java 17 or above
* Maven 3.6+
* AWS Account (for S3 and RDS setup)
* PostgreSQL Database (hosted on AWS RDS)
* An S3 Bucket on AWS


## **Project Setup**


### **1. Clone the Repository**


```
git clone https://github.com/your-username/Dropbox-Equivalent.git
cd Dropbox-Equivalent
```



### **2. Set Up PostgreSQL on AWS RDS**



* Set up a PostgreSQL instance on AWS RDS.
* Note down the endpoint, username, and database name.


### **3. AWS S3 Configuration**



* Create an S3 bucket in AWS.
* Store your AWS credentials securely. These will be used to configure access to the S3 bucket.


### **4. Configure Application Properties**

In the `src/main/resources/application.properties` file, configure the following properties:


```
aws.accessKeyId=your_access_key
aws.secretKey=your_secret_key
s3.bucketName=your_bucket_name
spring.datasource.url=jdbc:postgresql://your_rds_endpoint:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```



### **5. Build and Run the Application**

You can build and run the application using Maven:


```
mvn clean install
mvn spring-boot:run
```



### **6. Access the Application**

Once the service is running, you can access the APIs using Postman or any other REST client. The base URL for the APIs is:


```
http://localhost:8080/
```



## **API Endpoints**


### **Upload a File**


```
POST /files/upload

```



* **Request Body**: Multipart form data (`file`, `metadata`)


### **Retrieve a File**


```
GET /files/{fileName}

```



* **Path Parameter**: `fileName` - The name of the file to retrieve


### **Delete a File**


```
DELETE /files/{fileName}

```



* **Path Parameter**: `fileName` - The name of the file to delete


### **List All Files**


```
GET /files

```



* **Response**: List of file names stored in the S3 bucket


## **Contact**

For any inquiries or issues, please contact me at rameshwar.arrabelli2000@gmail.com.
