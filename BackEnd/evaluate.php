<?php
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
 //   while ($fruit = current($arr)) {
 //       printf("%s <br />", $fruit["userId"]);
 //       printf("%s <br />", $fruit["userImage"]);
//		$userId = $fruit["userId"];		
 //   next($arr);
//}
	//$num = count($arr); 
    //echo $num;	
    
	//test
	//$arr = array("a" => array("gId"=>"3","uId"=>"3","aId"=>"2","zan"=>"5","remarks"=> "evaluate"),"b" => array("gId"=>"3","uId"=>"2","aId"=>"2","zan"=>"5","remarks"=> "evaluate"));	
	//获取值
	while($a = current($arr)){
		$gId = $a['gId'];
		$uId = $a['uId'];
		$aId = $a['aId'];
		$zan = $a['zan'];
		$remarks = $a['remarks'];
		
		$find_repeat = "select * from organization where gId='".$gId."' and uId='".$uId."' and aId='".$aId."'";
		$result = mysqli_query ( $connect, $find_repeat );
		if(mysqli_num_rows($result) != 0){
			return response::show(405,'false','the data is repeated');
		}else{
			$sql = "INSERT INTO organization (gId, uId, aId, zan, remarks)
					VALUES ( '".$gId."' ,'".$uId."' , '".$aId."' ,'".$zan."' ,'".$remarks."')";
			if ($connect->multi_query($sql) === TRUE) {
			     next($arr);
		    } else {
			     return response::show(400,'false','data deliver fail');
		    }		
		}	
	}	
	return response::show(200,'true','evaluate success');	
	$connect->close();
?> 