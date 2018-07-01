<?php
	 function my_sort($arrays,$sort_key,$sort_order=SORT_ASC,$sort_type=SORT_NUMERIC ){  
        if(is_array($arrays)){  
            foreach ($arrays as $array){  
                if(is_array($array)){  
                    $key_arrays[] = $array[$sort_key];  
                }else{  
                    return false;  
                }  
            }  
        }else{  
            return false;  
        } 
        array_multisort($key_arrays,$sort_order,$sort_type,$arrays);  
        return $arrays;  
    } 
	
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();
    
	$json = file_get_contents ( 'php://input' );  
	$obj = json_decode ( $json,true ); 
	
	$userId = $obj['userId'];
	
	//$userId="1";
	
	$query_userScore="select uId, starNum from organization";
	$result_userScore=mysqli_query($connect, $query_userScore);
	$query_userComment="select userid, score from score";
	$result_userComment=mysqli_query($connect, $query_userComment);
	$rows_scoreinfo;
	$rows_commentinfo;
	$itera = 0;
	if(mysqli_num_rows($result_userScore) !== 0){
		while ( $row_1 = mysqli_fetch_row($result_userScore)) {
		$rows_scoreinfo[]=array(
		'userId'=>$row_1[0],
        'userScore'=>$row_1[1],
		);
		$itera++;
		}
	}
	if(mysqli_num_rows($result_userComment) !== 0){
		while ( $row_2 = mysqli_fetch_row($result_userComment)) {
			$rows_commentinfo[]=array(
		'userId'=>$row_2[0],
        'userScore'=>$row_2[1],
		);
		$itera++;
		}
	}
	
	$row_all = array_merge($rows_scoreinfo,$rows_commentinfo);
	
	
	//return response::show(200,"a",$row_all);
	
	if(!is_null($row_all) and !empty($row_all)){
		$item=array();
		$useridlist = array();
		foreach($row_all as $k=>$v){
			//return response::show(200,"c",$v);
			if(!isset($item[$v['userId']])){
				$item[$v['userId']]=$v;
			}else{
				$item[$v['userId']]['userScore']+=(int)$v['userScore'];
				$useridlist[] = $v['userId'];
			}
		}
		
		array_multisort(array_column($item,'userScore'),SORT_DESC,$item);
		
		$final_data = array(-1=>1);
		$ii = 0;
		$temp_data = array();
		for ($x=0; $x<10; $x++) {
			if(isset($item[$x])){
				$userid_temp = $item[$x]['userId'];
				$userscore_temp = $item[$x]['userScore'];
				$username_temp;
				$userimage_temp;
				$query_userget="select userName, userImage from user where userId = ".(int)$userid_temp;
				$result_userget=mysqli_query($connect, $query_userget);
	
				if(mysqli_num_rows($result_userget) !== 0){
					$row_3 = mysqli_fetch_row($result_userget);
					$username_temp = $row_3[0];
					$userimage_temp = $row_3[1];
				
				}
				
				$temp_data = array(
					'userId' => $userid_temp,
					'userScore' => (int)$userscore_temp,
					'userName' => $username_temp,
					'userImage' => $userimage_temp
				);
				
				$final_data[] = $temp_data;
				$ii++;
			}else{
				break;
			}
		} 
			
		$userownrank = -1;	
		foreach($final_data as $key => $value){
			//return response::show(200,"e",$final_data);
			if(isset($value['userId']) and (int)$value['userId'] == (int)$userId){
				$userownrank = (int)$key;
			}
		}
			
		if(isset($userId)){
			$userinfo = array();
		$query_userownstar="select sum(starNum) from organization where uId = ".(int)$userId." group by uId";
		$result_userown=mysqli_query($connect, $query_userownstar);
	
		if(mysqli_num_rows($result_userown) !== 0){
			$row_userownstar = mysqli_fetch_row($result_userown);
			$userownstar = (int)$row_userownstar[0];
		}
		$query_userownscore="select sum(score) from score where userid = ".(int)$userId." group by userid" ;
		$result_userscore=mysqli_query($connect, $query_userownscore);
	
		if(mysqli_num_rows($result_userscore) !== 0){
			$row_userownscore = mysqli_fetch_row($result_userscore);
			$userownscore = (int)$row_userownscore[0];
		}
		$userscore = $userownscore+$userownstar;
		$query_userowninfo="select userName, userImage from user where userId = ".(int)$userId;
		$result_userinfo=mysqli_query($connect, $query_userowninfo);
	
		if(mysqli_num_rows($result_userinfo) !== 0){
			$row_userowninfo = mysqli_fetch_row($result_userinfo);
		
			$userinfo = array(
				'userId' => $userId,
				'userName' => $row_userowninfo[0],
				'userImage' => $row_userowninfo[1],
				'userScore' => $userscore,
				'userRank' => $userownrank
			);
			
		}
		if(isset($userinfo) and !is_null($userinfo) and !empty($userinfo))
		$final_data["me"] = $userinfo;
		}
		
		return response::show(200,"calculate success",$final_data);
	}else{
		return response::show(200,'no data');
	}
?>