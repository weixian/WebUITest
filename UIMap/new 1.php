 // *****************************************************************************************
 // Contributed code - lightbulb
 // modified - chenlimei@20140310
 // *****************************************************************************************
 /*
   function: importTestCaseDataFromSpreadsheet
             convert a XLS file to XML, and call importTestCaseDataFromXML() to do import.
 
   args: db [reference]: db object
         fileName: XLS file name
         parentID: testcases parent node (container)
         tproject_id: testproject where to import testcases 
         userID: who is doing import.
         bRecursive: 1 -> recursive, used when importing testsuites
         [importIntoProject]: default 0
         
   
   returns: map 
 
   rev:
       Original code by lightbulb.
       Refactoring by franciscom
 */
 function importTestCaseDataFromSpreadsheet(&$db,$fileName,$parentID,$tproject_id,
                                            $userID,$bRecursive,$importIntoProject = 0)
 {
         
		 $xmlTCs = null;
         $resultMap  = null;
         $xml_filename=$fileName . '.xml';
		 global $args;
		 if($args->useRecursion)
		 {	
			create_xml_tsspec_from_xls($fileName,$xml_filename);
		 }
		 else
		 {
			create_xml_tcspec_from_xls($fileName,$xml_filename);
		}
         $resultMap=importTestCaseDataFromXML($db,$xml_filename,$parentID,$tproject_id,$userID,
                                              $bRecursive,$importIntoProject);
         unlink($fileName);
         unlink($xml_filename);
         
         return $resultMap;
 }
 
 // --------------------------------------------------------------------------------------
 /*
   function: create_xml_tcspec_from_xls
             Using an XSL file, that contains testcase specifications
             creates an XML testlink test specification file.
             
             XLS format:
             Column       Description
               1          test case name
               2          summary
               3          steps
               4          expectedresults
               
             First row contains header:  name,summary,steps,expectedresults
             and must be skipped.
             
   args: xls_filename
         xml_filename
   
   returns: 
 */
 function create_xml_tcspec_from_xls($xls_filename, $xml_filename) 
 {		
		 //echo $xls_filename;
         define('FIRST_DATA_ROW',2);
         define('IDX_COL_NAME',3);
         define('IDX_COL_SUMMARY',4);
		 define('IDX_COL_PRECONDITIONS',5);
         define('IDX_COL_STEPS',6);
         define('IDX_COL_EXPRESULTS',7);
		 define('IDX_COL_IMPORTANCE',9);
   
         $xls_handle = new Spreadsheet_Excel_Reader(); 
   
         $xls_handle->setOutputEncoding('UTF-8'); 
         $xls_handle->read($xls_filename);
         $xls_rows = $xls_handle->sheets[0]['cells'];
         $xls_row_qty = sizeof($xls_rows);
		 //echo '$xls_row_qty:'.$xls_row_qty;
   
         if($xls_row_qty < FIRST_DATA_ROW)
         {
         return;  
   }
 
         $xmlFileHandle = fopen($xml_filename, 'w') or die("can't open file");
		 fwrite($xmlFileHandle,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
         fwrite($xmlFileHandle,"<testcases>\n");
 
         for($idx = FIRST_DATA_ROW;$idx <= $xls_row_qty; $idx++)
         {   
			 $iStepNum = 1;
			 //echo $idx;
             //$name = htmlspecialchars(iconv("CP1252","UTF-8",$xls_rows[$idx][IDX_COL_NAME]));
			 $name = str_replace('?',"...",$xls_rows[$idx][IDX_COL_NAME]);
			 If ($name <> "")
			 {
				$iStepNum = 1;
				if ($idx != FIRST_DATA_ROW)
				{
					fwrite($xmlFileHandle,"</steps>\n");
					fwrite($xmlFileHandle,"</testcase>\n");
				}
				fwrite($xmlFileHandle,"<testcase internalid=\"\" name=" . '"' . $name. '"'.">\n");
             
                   // $summary = htmlspecialchars(iconv("CP1252","UTF-8",$xls_rows[$idx][IDX_COL_SUMMARY]));
				// 20090117 - contribution - BUGID 1992
				$summary = str_replace('?',"...",$xls_rows[$idx][IDX_COL_SUMMARY]);  
				$summary = nl2p($summary);
                //$summary = nl2p(htmlspecialchars(iconv("CP1252","UTF-8", $summary)));
				fwrite($xmlFileHandle,"<summary><![CDATA[" . $summary . "]]></summary>\n");
				
				$preConditions = str_replace('?',"...",$xls_rows[$idx][IDX_COL_PRECONDITIONS]);  
				$preConditions = nl2p($preConditions);
                //$preConditions = nl2p(htmlspecialchars(iconv("CP1252","UTF-8", $preConditions)));
				fwrite($xmlFileHandle,"<preconditions><![CDATA[" . $preConditions . "]]></preconditions>\n");
				
				$importance = str_replace('?',"...",$xls_rows[$idx][IDX_COL_IMPORTANCE]);  
				$importance = nl2p($importance);
                //$importance = nl2p(htmlspecialchars(iconv("CP1252","UTF-8", $importance)));
				fwrite($xmlFileHandle,"<importance><![CDATA[" . $importance . "]]></importance>\n");
				fwrite($xmlFileHandle,"<steps>\n");
				fwrite($xmlFileHandle,"<step>\n");
				
				fwrite($xmlFileHandle,"<step_number><![CDATA[".$iStepNum."]]></step_number>\n");
				$step = str_replace('?',"...",$xls_rows[$idx][IDX_COL_STEPS]);
				$step = nl2p($step);
				//$step = nl2p(htmlspecialchars(iconv("CP1252","UTF-8",$steps)));
				fwrite($xmlFileHandle,"<actions><![CDATA[".$step."]]></actions>\n");
				
				$expresults = str_replace('?',"...",$xls_rows[$idx][IDX_COL_EXPRESULTS]);
				$expresults = nl2p(htmlspecialchars($expresults));
				//$expresults = nl2p(htmlspecialchars(iconv("CP1252","UTF-8",$expresults)));
				fwrite($xmlFileHandle,"<expectedresults><![CDATA[".$expresults."]]></expectedresults>\n");
				
				fwrite($xmlFileHandle,"</step>\n");
			 }
             else
			 {
				fwrite($xmlFileHandle,"<step>\n");
				$iStepNum++;
				
				fwrite($xmlFileHandle,"<step_number><![CDATA[".$iStepNum."]]></step_number>\n");
				$step = str_replace('?',"...",$xls_rows[$idx][IDX_COL_STEPS]);
				$step = nl2p($step);
				//$step = nl2p(htmlspecialchars(iconv("CP1252","UTF-8",$steps)));
				fwrite($xmlFileHandle,"<actions><![CDATA[".$step."]]></actions>\n");
				
				$expresults = str_replace('?',"...",$xls_rows[$idx][IDX_COL_EXPRESULTS]);
				$expresults = nl2p(htmlspecialchars($expresults));
				//$expresults = nl2p(htmlspecialchars(iconv("CP1252","UTF-8",$expresults)));
				fwrite($xmlFileHandle,"<expectedresults><![CDATA[".$expresults."]]></expectedresults>\n");
				
				fwrite($xmlFileHandle,"</step>\n");
             }
         }
		 fwrite($xmlFileHandle,"</steps>\n");
		 fwrite($xmlFileHandle,"</testcase>\n");
         fwrite($xmlFileHandle,"</testcases>\n");
         fclose($xmlFileHandle);

 }
 
 // --------------------------------------------------------------------------------------
 /*
   function: create_xml_tsspec_from_xls
             Using an XSL file, that contains more than one sheet, with each sheet a testsuite
             creates an XML testlink test specification file.
             
             XLS format:               
             First row contains header:  name,summary,steps,expectedresults
             and must be skipped.
             
   args: xls_filename
         xml_filename
   
   returns: 
 */
 function create_xml_tsspec_from_xls($xls_filename, $xml_filename) 
 {		
	//echo $xls_filename;
    define('FIRST_DATA_ROW',2);
	define('IDX_COL_TESTSUITES',1);
    define('IDX_COL_NAME',3);
    define('IDX_COL_SUMMARY',4);
	define('IDX_COL_PRECONDITIONS',5);
    define('IDX_COL_STEPS',6);
    define('IDX_COL_EXPRESULTS',7);
	define('IDX_COL_IMPORTANCE',9);
   
    $xls_handle = new Spreadsheet_Excel_Reader(); 
   
    $xls_handle->setOutputEncoding('UTF-8'); 
    $xls_handle->read($xls_filename);
	
	$xmlFileHandle = fopen($xml_filename, 'w') or die("can't open file");
	fwrite($xmlFileHandle,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	fwrite($xmlFileHandle,"<testsuite name=\"\" >\n");
		 
	//读取多张sheet
	for($sheet=0;$sheet<count($xls_handle->sheets);$sheet++)
	{
		//echo '<br>'.count($xls_handle->sheets);
		//echo '<br>'.$sheet;
		//if($xls_handle->sheets[$sheet] == SPREADSHEET_EXCEL_READER_TYPE_EOF)
		if($xls_handle->sheets[$sheet]['numRows'] <1)
		{
			//echo 'break';
			continue;
		}
         $xls_rows = $xls_handle->sheets[$sheet]['cells'];
         $xls_row_qty = sizeof($xls_rows);
		 
   
         if($xls_row_qty < FIRST_DATA_ROW)
         {
         return;  // >>>----> bye!
		}
		$idx = FIRST_DATA_ROW;
		//echo '<br>$idx:'.$idx;
		$suite = str_replace('?',"...",$xls_rows[$idx][IDX_COL_TESTSUITES]);
		$suiteArray = explode("/", $suite);
		$nodeNum = count($suiteArray);
		for($iSuite = 0; $iSuite < $nodeNum; $iSuite++)
		{
			fwrite($xmlFileHandle,"<testsuite name=" . '"' . $suiteArray[$iSuite]. '"'.">\n");
		}
         //fwrite($xmlFileHandle,"<testcases>\n");
		 $iStepNum = 1;
         for($idx = FIRST_DATA_ROW;$idx <= $xls_row_qty; $idx++ )
         {   
			$suite = str_replace('?',"...",$xls_rows[$idx][IDX_COL_TESTSUITES]);
			if($suite <> ""and $idx != FIRST_DATA_ROW)
			{	
				fwrite($xmlFileHandle,"</steps>\n");
				fwrite($xmlFileHandle,"</testcase>\n");
				//fwrite($xmlFileHandle,"</testcases>\n");
				for($iSuite = 0; $iSuite < $nodeNum; $iSuite++)
				{
					fwrite($xmlFileHandle,"</testsuite>\n");
				}
				$suiteArray = explode("/", $suite);
				$nodeNum = count($suiteArray);
				for($iSuite = 0; $iSuite < $nodeNum; $iSuite++)
				{
					fwrite($xmlFileHandle,"<testsuite name=" . '"' . $suiteArray[$iSuite]. '"'.">\n");
				}
				//fwrite($xmlFileHandle,"<testcases>\n");
			}
			
			
			 //$name = htmlspecialchars(iconv("CP1252","UTF-8",$xls_rows[$idx][IDX_COL_NAME]));
			 $name = str_replace('?',"...",$xls_rows[$idx][IDX_COL_NAME]);
			 $name = htmlspecialchars($name);			 
			 
			 If ($name <> "")
			 {
				$iStepNum = 1;
				//echo '<br>$idx:'.$idx;
				if ($idx != FIRST_DATA_ROW and $suite == "")
				{
					fwrite($xmlFileHandle,"</steps>\n");
					fwrite($xmlFileHandle,"</testcase>\n");
				}
				fwrite($xmlFileHandle,"<testcase internalid=\"\" name=" . '"' . $name. '"'.">\n");
			 
				   // $summary = htmlspecialchars(iconv("CP1252","UTF-8",$xls_rows[$idx][IDX_COL_SUMMARY]));
				// 20090117 - contribution - BUGID 1992
				//$summary = $xls_rows[$idx][IDX_COL_SUMMARY];
				
				$summary = str_replace('?',"...",$xls_rows[$idx][IDX_COL_SUMMARY]);  
				//echo $summary;
				//$summary = nl2p(htmlspecialchars(iconv("CP1252","UTF-8", $summary)));
				$summary = nl2p(htmlspecialchars($summary));
				fwrite($xmlFileHandle,"<summary><![CDATA[" . $summary . "]]></summary>\n");
				
				$preConditions = str_replace('?',"...",$xls_rows[$idx][IDX_COL_PRECONDITIONS]);  
				//$preConditions = nl2p(htmlspecialchars(iconv("CP1252","UTF-8", $preConditions)));
				$preConditions = nl2p(htmlspecialchars($preConditions));
				fwrite($xmlFileHandle,"<preconditions><![CDATA[" . $preConditions . "]]></preconditions>\n");
				
				$importance = str_replace('?',"...",$xls_rows[$idx][IDX_COL_IMPORTANCE]);  
				//$importance = nl2p(htmlspecialchars(iconv("CP1252","UTF-8", $importance)));
				$importance = nl2p($importance);
				fwrite($xmlFileHandle,"<importance><![CDATA[" . $importance . "]]></importance>\n");
				fwrite($xmlFileHandle,"<steps>\n");
				fwrite($xmlFileHandle,"<step>\n");
				
				fwrite($xmlFileHandle,"<step_number><![CDATA[".$iStepNum."]]></step_number>\n");
				$step = str_replace('?',"...",$xls_rows[$idx][IDX_COL_STEPS]);
				$step = nl2p(htmlspecialchars($step));
				//$step = nl2p(htmlspecialchars(iconv("CP1252","UTF-8",$step)));
				fwrite($xmlFileHandle,"<actions><![CDATA[".$step."]]></actions>\n");
				
				$expresults = str_replace('?',"...",$xls_rows[$idx][IDX_COL_EXPRESULTS]);
				//$expresults = nl2p(htmlspecialchars(iconv("CP1252","UTF-8",$expresults)));
				$expresults = nl2p(htmlspecialchars($expresults));
				fwrite($xmlFileHandle,"<expectedresults><![CDATA[".$expresults."]]></expectedresults>\n");
				
				fwrite($xmlFileHandle,"</step>\n");
			 }
			 else
			 {
				fwrite($xmlFileHandle,"<step>\n");
				$iStepNum++;
				
				fwrite($xmlFileHandle,"<step_number><![CDATA[".$iStepNum."]]></step_number>\n");
				$step = str_replace('?',"...",$xls_rows[$idx][IDX_COL_STEPS]);
				$step = nl2p($step);
				//$step = nl2p(htmlspecialchars(iconv("CP1252","UTF-8",$step)));
				fwrite($xmlFileHandle,"<actions><![CDATA[".$step."]]></actions>\n");
				
				$expresults = str_replace('?',"...",$xls_rows[$idx][IDX_COL_EXPRESULTS]);
				$expresults = nl2p($expresults);
				//$expresults = nl2p(htmlspecialchars(iconv("CP1252","UTF-8",$expresults)));
				fwrite($xmlFileHandle,"<expectedresults><![CDATA[".$expresults."]]></expectedresults>\n");
				
				fwrite($xmlFileHandle,"</step>\n");
			 }
			
		 }
		 fwrite($xmlFileHandle,"</steps>\n");
		 fwrite($xmlFileHandle,"</testcase>\n");
         //fwrite($xmlFileHandle,"</testcases>\n");
		 for($iSuite = 0; $iSuite < $nodeNum; $iSuite++)
		{
			fwrite($xmlFileHandle,"</testsuite>\n");
		}
	}
	fwrite($xmlFileHandle,"</testsuite>\n");
    fclose($xmlFileHandle);

 }