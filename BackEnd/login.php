<?php
require_once('./Db.php');
require_once('./response.php');
$connect = Db::connect();
 //获取客户端传入的json数据  
$json = file_get_contents ( 'php://input' );  
$obj = json_decode ( $json ); 

$sql = "select * from user where userName='caiwen' and userPassword='12345678'";
$result = mysqli_query ( $connect, $sql );
mysqli_close($connect);

if(!$result || mysqli_num_rows($result) == 0){
    return Response::show(400, 'fail');
}else{
	while($row = mysqli_fetch_assoc($result)) {
		$rows[] = $row;
	} 
	return response::show(200,'success',$rows);
}
	
exit;
?>