<?php  
  $servername = "192.168.43.234";
  $username = "root";
  $password = "123456789";
  $dbname = "redowl";

  $conn = new mysqli($servername, $username, $password, $dbname);
 
mysqli_set_charset($conn,"utf8");

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
  } 
  
if (mysqli_connect_errno($conn))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
$email = $_POST['email'];  
$pass = $_POST['pass'];


$sql = "insert into members( email, pass) values('$email','$pass')";

$result = mysqli_query($conn,$sql);


  if($result){  
    echo 'success';  
  }  
  else{  
    echo 'failure';  
  }  
  
  $conn->close();  
?> 
 
<html>
   <body>
   
      <form action = "<?php $_PHP_SELF ?>" method = "POST">
         Name: <input type = "text" name = "email" />
         Address: <input type = "text" name = "pass" />
         <input type = "submit" />
      </form>
   
   </body>
</html>


<!--
+----------+-----------------------+------+-----+---------+----------------
| Field    | Type                  | Null | Key | Default | Extra
+----------+-----------------------+------+-----+---------+----------------
| pid      | int(11)               | NO   | PRI | NULL    | auto_increment
| email    | varchar(50)           | NO   |     | NULL    |
| pass     | varchar(20)           | NO   |     | NULL    |
| phone    | varchar(20)           | NO   |     | NULL    |
| sex      | enum('male','female') | NO   |     | NULL    |
| join_dat | datetime              | NO   |     | NULL    |
+----------+-----------------------+------+-----+---------+----------------




-->