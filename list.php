<?php
define('HOST','192.168.0.16');
define('USER','root');
define('PASS','rcube1112');
define('DB','redowl');
 
$con = mysqli_connect(HOST,USER,PASS,DB);
 
$sql = "select * from list";
 
$res = mysqli_query($con,$sql);
 
$response = array();
 
while($row = mysqli_fetch_array($res)){
array_push($response,
array('writer'=>$row[1],'date'=>$row[2],'contents'=>$row[3]));
}
 
echo json_encode(array("response"=>$response));
 
mysqli_close($con);
 
?>

<!--
+----------+------------+------+-----+---------+----------------+
| Field    | Type       | Null | Key | Default | Extra          |
+----------+------------+------+-----+---------+----------------+
| tid      | int(11)    | NO   | PRI | NULL    | auto_increment |
| contents | text       | NO   |     | NULL    |                |
| writer   | int(11)    | NO   |     | NULL    |                |
| date     | datetime   | NO   |     | NULL    |                |
+----------+------------+------+-----+---------+----------------+


-->