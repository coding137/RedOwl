<?php
define('HOST','192.168.0.16');
define('USER','root');
define('PASS','rcube1112');
define('DB','redowl');
 
$con = mysqli_connect(HOST,USER,PASS,DB);
 
$sql = "select * from list";
 
$res = mysqli_query($con,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,
array('writer'=>$row[1],'date'=>$row[2],'contents'=>$row[3]));
}
 
echo json_encode(array("result"=>$result));
 
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