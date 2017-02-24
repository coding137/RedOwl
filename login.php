<?php 
 define('HOST','192.168.0.16');
 define('USER','root');
 define('PASS','rcube1112');
 define('DB','redowl');
 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');

 $email = $_POST['email'];
 $pass = $_POST['pass'];
 
 //Creating sql query
 $sql = "SELECT * FROM members WHERE email='$email' AND pass='$pass'";
 
 //importing dbConnect.php script 
 
 //executing query
 $result = mysqli_query($con,$sql);
 
 //fetching result
 $check = mysqli_fetch_array($result);
 
 //if we got some result 
 if(isset($check)){
 //displaying success 
 echo "success";
 }else{
 //displaying failure
 echo "failure";
 }
 mysqli_close($con);
  ?>