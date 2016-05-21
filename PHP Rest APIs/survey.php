<?php
 require_once('DB_Connect.php');
 $dbobj = new dbconnect();
 $con = $dbobj->connect();
 
 if($con == mysqli_connect_error())
	 echo "Error in connecting with the database";
 else{
	//----------ask survey question start----------
	
	//Get the combination needed for asking questions
	$combination = "Combination1";
	$query = "select CombID from combination where CombType= '$combination'";
	$result = mysqli_query($con,$query);
	
	//extract the combiination id
	$row_name = mysqli_fetch_assoc($result);
	$combID = $row_name["CombID"];
	

	// Get the different categories
	$query = "select CatID from comb_category where combID = $combID";
	$result = mysqli_query($con,$query);
        $rows = array();
	while($row_cat = mysqli_fetch_array($result))
		$rows[] = $row_cat;
        
	$iterator = 0;
	//Iterate and get the min and max values of the mysql database stored
	foreach($rows as $row_cat){
		$value = $row_cat["CatID"];
		//getting the min value
		$minquery = "select min(SID) as m from surveyquestions where CATID = $value";
		$result = mysqli_query($con,$minquery);
		$row_name = mysqli_fetch_assoc($result);
		$minValue = $row_name["m"];
		
		//getting the max value
		$maxquery = "select max(SID) as m from surveyquestions where CATID =$value";
		$result = mysqli_query($con,$maxquery);
		$row_name = mysqli_fetch_assoc($result);
		$maxValue = $row_name["m"];
		
                

		$randomID = rand($minValue,$maxValue);
                
		//fetch the data 
		$query = "SELECT questions FROM surveyquestions WHERE SID = $randomID";

		$results_num_ques = mysqli_query($con,$query);
	
		while($row_num_ques = mysqli_fetch_assoc($results_num_ques))
		{
			$ques[$iterator] = $row_num_ques["questions"];	
		}
		$iterator++;
	}

        $surveyqst = null;
	for($it =0;$it < count($ques);$it++){
		if($it == 0){
			$surveyqst = $ques[$it];
		}
		else{
			$surveyqst = $surveyqst .":" . $ques[$it];
		}
	}
	echo $surveyqst;
 }
 
 	
	 
?>				