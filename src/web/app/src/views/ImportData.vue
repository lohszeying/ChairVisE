<template>
  <el-main>
    <el-alert
      title="You need to login-in to view the page"
      type="error"
      v-if="!isLogin && !isAppLoading">
      &nbsp;<el-button type="warning" plain size="mini" @click="navigateToHomePage">Return to the Home Page</el-button>
    </el-alert>

    <div v-if="isLogin">
      <mapping-tool v-if="isReadyForMapping" ref="mapTool"></mapping-tool>

      <el-card v-else>
        <div slot="header" class="clearfix">
          <span>Upload Data</span>
        </div>
      
      <div class="section">
        <h2> Record Information </h2>
        <el-divider></el-divider>
        
        <div class="form-card">
          <label class="label"> Conference Type </label>
          <br/>
          <el-radio-group v-model="formatType" size="medium">
            <el-radio-button :label="1">EasyChair</el-radio-button>
            <el-radio-button :label="2">SoftConf</el-radio-button>
          </el-radio-group>
        </div>

        <div class="form-card">
          <label class="label"> Table Type </label>
          <br/>
          <el-radio-group v-model="tableType" size="medium">
            <el-radio-button v-for="(schema, idx) in dbSchemas" 
              :label="idx" 
              :key="schema.name"> 
              {{ schema.name }}
            </el-radio-button>
          </el-radio-group>
        </div>
      </div>

      <div class="section" v-if="isReadyForChoosing">
        <h2>
          Mapping Information

          <el-tooltip placement="top">
            <div slot="content">
              Optional
            </div>
            <el-button type="text" icon="el-icon-info" circle></el-button>
          </el-tooltip>
        </h2>
        <el-divider></el-divider>

        <div class="form-card">
          <el-switch
            v-model="hasHeader"
            active-text="Has Header"
            inactive-text="No Header">
          </el-switch>
        </div>

        <div class="form-card" >
          <el-switch
            v-model="hasPredefined"
            active-text="Predefined Mapping"
            inactive-text="No Predefined Mapping">
          </el-switch>
        </div> 
      </div>
      
      <div class="section" v-if="isReadyForChoosing">
        <h2> 
          Version Information

          <el-tooltip placement="top">
            <div slot="content">
              If the date is based on an existing conference of the same year, current record will be replaced based on record type.
              <br/>
              If the conference or the date of the conference is on a different year, current record will be created based on record type.
            </div>
            <el-button type="text" icon="el-icon-question" circle></el-button>
          </el-tooltip>
        </h2>        
        <el-divider></el-divider>

        <el-row class="form-card">
          <el-col>
            <label class="label">
              Title
            </label>
            <br/>
            <el-autocomplete
                class="inline-input"
                v-model="conferenceTitle"
                :fetch-suggestions="querySearchConference"
                placeholder="Title of Conference"
            ></el-autocomplete>
          </el-col>
        </el-row>
        
        <el-row class="form-card">
          <el-col>
            <label class="label">
              Date
            </label>
            <br/>
            <el-input
              type="date"
              v-model="versionDate"
              placeholder="Input Date"
            ></el-input>
          </el-col>
        </el-row>
        <div class="form-card">
          <el-upload v-if="isReadyForUpload" drag action=""
                    :auto-upload="false"
                    :show-file-list="false"
                    :multiple="false"
                    :on-change="fileUploadHandler">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">Drop .csv file here or <em>click to upload</em></div>
          </el-upload>
        </div>
      </div>

      </el-card>
    </div>

    <!-- Newly added, Will tell user to only upload .csv file -->
    <el-dialog
        title="Upload failed"
        :visible.sync="isFailure"
        width="30%" center>
      <span>Please only submit .csv file.</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" v-on:click="isFailure = false">Go Back</el-button>
      </span>
    </el-dialog>

  </el-main>
</template>

<script>
  import MappingTool from "@/components/MappingTool.vue";
  import Papa from "papaparse";
  import {REVIEW_DATE_DAY_FIELD, REVIEW_DATE_TIME_FIELD, REVIEW_TABLE_ID} from "@/common/const"
  import {deepCopy} from "@/common/utility"
  import PredefinedMappings from "@/store/data/predefinedMapping"
  import moment from "moment"
  
  export default {
    name: "ImportData",
    data() {
      return {
        predefinedMappings: PredefinedMappings,
        isFailure: false
      };
    },
    beforeCreate() {
      this.$store.dispatch('fetchDBMetaDataEntities');
      this.$store.dispatch('getConferenceList');
      //Set HasHeader and PredefinedSwitch to true by default
      this.$store.commit("setHasHeader", true);
      this.$store.commit("setPredefinedSwitch", true);
    },
    computed: {
      isLogin() {
        return this.$store.state.userInfo.isLogin;
      },
      isAppLoading: function () {
        return this.$store.state.isPageLoading || this.$store.state.dbMetaData.entitiesStatus.isLoading;
      },
      dbSchemas: function () {
        return this.$store.state.dbMetaData.entities;
      },
      formatType: {
        get: function () {
          return this.$store.state.dataMapping.data.formatType;
        },
        set: function (newValue) {
          this.$store.commit("setFormatType", newValue);
        }
      },
      tableType: {
        get: function () {
          return this.$store.state.dataMapping.data.tableType;
        },
        set: function (newValue) {
          let dbSchema = deepCopy(this.dbSchemas[newValue]);
          if (newValue === REVIEW_TABLE_ID) {
            dbSchema.fieldMetaDataList.push(REVIEW_DATE_DAY_FIELD);
            dbSchema.fieldMetaDataList.push(REVIEW_DATE_TIME_FIELD);
          }
          this.$store.commit("setTableType", newValue);
          this.$store.commit("setDBSchema", dbSchema);
        }
      },
      conferenceTitle: {
        get: function () {
          //return this.$store.state.dataMapping.data.conferenceTitle;
          this.$store.state.conference.conferenceForm.title;
          return this.$store.state.dataMapping.data.conferenceTitle;
        },
        set: function (value) {
          if (value !== '') {
            this.$store.commit('setConferenceFormField', {
              field: 'title',
              value
            });
            this.$store.commit("setConferenceTitle", value);
          } else {
            this.$store.commit('setConferenceFormField', {
              field: 'title',
              value
            });
            this.$store.commit("clearConferenceTitle");
          }
        }
      },
      versionDate: {
        get: function () {
          this.$store.state.version.versionForm.date;
          //console.log("version form: " + this.$store.state.version.versionForm.ver_date);
          //console.log("dataMapping version date: " + this.$store.state.dataMapping.data.versionDate);
          return this.$store.state.dataMapping.data.versionDate;
        },
        set: function (value) {
          if (value !== '') {
            this.$store.commit('setVersionFormField', {
              field: 'date',
              value
            })
            this.$store.commit("setVersionDate", value);
          } else {
            this.$store.commit('setVersionFormField', {
              field: 'date',
              value
            })
            this.$store.commit("clearVersionDate");
          }
        }
      },
      hasHeader: {
        get: function () {
          return this.$store.state.dataMapping.data.hasHeader;
        },
        set: function (newValue) {
          this.$store.commit("setHasHeader", newValue);
        }
      },
      hasPredefined: {
        get: function () {
          return this.$store.state.dataMapping.data.hasPredefined;
        },
        set: function (newValue) {
          this.$store.commit("setPredefinedSwitch", newValue);
        }
      },
      predefinedMappingId: {
        get: function () {
          return this.$store.state.dataMapping.data.predefinedMappingId;
        },
        set: function (newValue) {
          this.$store.commit("setPredefinedMapping", {id: newValue, mapping: PredefinedMappings[newValue].mapping});
        }
      },
      isReadyForMapping: function () {
        return this.$store.state.dataMapping.hasFileUploaded
          && this.$store.state.dataMapping.hasFormatTypeSpecified
          && this.$store.state.dataMapping.hasTableTypeSelected
          && this.$store.state.dataMapping.hasHeaderSpecified
          && this.$store.state.dataMapping.hasPredefinedSpecified
          && this.$store.state.dataMapping.hasConferenceTitleSpecified
          && this.$store.state.dataMapping.hasVersionDateSpecified;
      },
      uploaded: function () {
        return this.$store.state.dataMapping.hasFileUploaded;
      },
      isReadyForUpload: function () {
        return this.$store.state.dataMapping.hasFormatTypeSpecified
         && this.$store.state.dataMapping.hasTableTypeSelected
         && this.$store.state.dataMapping.hasHeaderSpecified
         && this.$store.state.dataMapping.hasPredefinedSwitchSpecified
         && this.$store.state.dataMapping.hasConferenceTitleSpecified
         && this.$store.state.dataMapping.hasVersionDateSpecified;
      },
      isReadyForChoosing: function () {
        return this.$store.state.dataMapping.hasTableTypeSelected;
      }
    },
    methods: {
      querySearchConference(queryString, cb) {
        // convert to array of string
        var links = this.$store.state.conference.conferenceList.map(c => c.title);
        // function to remove duplicate from array of string
        let reduceFunction = (links) => links.filter((c,i) => links.indexOf(c) === i );
        links = reduceFunction(links);
        links = links.map(c => { return { "value" : c} });
        var results = queryString ? links.filter(this.createFilter(queryString)) : links;
        cb(results);
      },
      createFilter(queryString) {
        return (link) => {
          return (link.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      navigateToHomePage() {
        this.$router.replace("/home");
      },
      fileUploadHandler: function (file) {
        //If file extension is not .csv, do not allow user to proceed
        if(file.name.split(".").pop() != 'csv'){
          // eslint-disable-next-line no-console
          //console.log('file is not .csv, prompt will show to tell user to upload .csv file');
          this.isFailure = true;
          return;
        } else {
          //Can proceed

          // eslint-disable-next-line no-console
          //console.log('file is .csv');
          this.isFailure = false;
        }

        // show loading and go parsing
        this.$store.commit("setPageLoadingStatus", true);


        /* WORK IN PROGRESS */
        //If conference list is empty, then create conference.
        //console.log(this.$store.state.conference.conferenceList);
        if (!this.$store.state.conference.conferenceList) {
          //console.log("don't have");
        } else {
          var confExist = false;
          var confId = 0;

          console.log("current user: " + this.$store.state.userInfo.userEmail);

          for (var i = 0; i < this.$store.state.conference.conferenceList.length; i++) {
            if (this.conferenceTitle.toLowerCase() === this.$store.state.conference.conferenceList[i].title.toLowerCase() &&
                this.$store.state.userInfo.userEmail === this.$store.state.conference.conferenceList[i].userEmail) {
              //Exist, don't save conference
              console.log("conference user: " + this.$store.state.conference.conferenceList[i].userEmail);
              confExist = true;
              confId = this.$store.state.conference.conferenceList[i].id;
              break;
            }
          }
          if (!confExist) {
            console.log("didnt exist");
            // if conf doesn't exist, save conf and save version at same time
            this.$store.dispatch('saveConference').then(() => {

              let newConfId = this.$store.state.conference.conferenceList[this.$store.state.conference.conferenceList.length - 1].id;

              console.log("new id: " + newConfId);
              this.$store.dispatch('saveVersion', newConfId).then(() => {
                const versionId = this.$store.state.version.versionList[this.$store.state.version.versionList.length - 1].id;
                console.log("New version id: " + versionId);
                this.$store.commit('setVersionId', versionId);
                console.log(this.$store.state.dataMapping.data.versionId);
              });

            });

            //Then add to version
          } else {
            //Existed, add to version

            console.log("exist");
            console.log("conf ID: " + confId);
            //console.log("current date: " + this.versionDate);
            let isReplaced = false;

            this.$store.dispatch('getVersionList', confId).then( () => {
              //console.log("version list: " + this.$store.state.version.versionList[0].date);


              for (let j = 0; j < this.$store.state.version.versionList.length; j++) {
                let existingDate = this.$store.state.version.versionList[j].date.substring(0, 4);
                let newDate = this.versionDate.substring(0, 4);

                console.log("existing date: " + existingDate);
                console.log("new date: " + newDate);
                console.log(newDate === existingDate);

                if (existingDate === newDate) {
                  let versionId = this.$store.state.version.versionList[j].id;
                  console.log("conf id: " + confId);
                  console.log("version id: " + versionId);
                  this.$store.commit('setVersionId', versionId);
                  this.$store.dispatch('updateVersion', {conferenceId: confId, versionId: versionId});
                  isReplaced = true;
                  break;
                }
              }

              if (!isReplaced) {
                this.$store.dispatch('saveVersion', confId);
              }
            });
            console.log("version form: " + this.$store.state.version.versionForm.date);

          }
        }


        // if versionList is empty
        // console.log(this.$store.state.presentation.versionList);
        // filter by "AuthorRecord" "ReviewRecord" "SubmissionRecord"
        if (!this.$store.state.presentation.versionList) {
          //console.log("here 1");
          this.$store.commit("setIsNewVersion", false);
        } else {
          // if tabletype 0 author elif 1 review elif 2 sub
          // filter by "AuthorRecord" "ReviewRecord" "SubmissionRecord"
          //console.log("here 2");
          var verList;
          switch (this.$store.state.dataMapping.data.tableType) {
            case 0:
              verList = this.$store.state.presentation.versionList
                        .filter(v => v.recordType === "AuthorRecord")
                        .map(v => v.versionId);
              break;
            case 1:
              verList = this.$store.state.presentation.versionList
                        .filter(v => v.recordType === "ReviewRecord")
                        .map(v => v.versionId);
              break;
            case 2:
              verList = this.$store.state.presentation.versionList
                        .filter(v => v.recordType === "SubmissionRecord")
                        .map(v => v.versionId);
              break;
            default:
          }
          this.$store.commit("setIsNewVersion",
                            !verList.includes(this.$store.state.dataMapping.data.versionId));
        }

        /*  tabletype 1	  1 - review 
            tabletype 0	  2 - author
            tabletype 2	  3 - sub */
        // map sub to sub// rev to rev // author to author if predefined mapping specified
        if (this.$store.state.dataMapping.data.hasPredefined) {
            switch(this.$store.state.dataMapping.data.tableType) {
              case 0:
                this.$store.commit("setPredefinedMapping",
                {id: 2, mapping: PredefinedMappings[2].mapping});
                break;
              case 1:
                this.$store.commit("setPredefinedMapping",
                {id: 1, mapping: PredefinedMappings[1].mapping});
                break;
              case 2:
                this.$store.commit("setPredefinedMapping",
                {id: 3, mapping: PredefinedMappings[3].mapping});
                break;
              default:
            }
        }
        else {
            this.$store.commit("setPredefinedMapping",
             {id: 0, mapping: PredefinedMappings[0].mapping});
        }

        Papa.parse(file.raw, {
          // ignoring empty lines in csv file
          skipEmptyLines: true,
          complete: function (result) {
          var res=result;
          var res2=res.data;
          var verId = this.$store.state.dataMapping.data.versionId;

          //author file preprocessing
          if( this.$store.state.dataMapping.data.tableType=="0" ){
              var authorres=[];
              //ACL file preprocessing //Softconf
              if(this.$store.state.dataMapping.data.formatType=="2"){
              authorres.push(["submission #","first name","last name","email","country","organization","Web page","person #","corresponding?"]);
              // for each row of data, manipulate temporary array element[] 
              // then push to true array res2[] for parsing
                for (var i = 1; i < res2.length; i++) {
                  var x = res2[i];
                  //console.log(x);
                  var k=0,j=14,element=[],corr="",country="";
                  while(x[j]!=""){
                    if(x[j]==x[65] && x[j+1]==x[66]){
                    corr = "yes";
                    country=x[78];
                    }
                    else {
                    corr="no";
                    country="";
                    }
                    element[k]= [x[0],x[j],x[j+1],x[j+2],country,x[j+3],"","",corr, verId];
                    authorres.push(element[k]);
                    k+=1;
                    j=14+k*5;
                  }
                //var element1=[x[0],x[14],x[15],x[16],"",x[17],"",""];
                }
                res2=authorres;
                //console.log(authorres)
              }

              //author anonymization - Both formats
              var convertstring=require("convert-string");
              for(var m=1;m<res2.length;m++){
                  var conv1=convertstring.stringToBytes(res2[m][1]);
                  var conv2=convertstring.stringToBytes(res2[m][2]);
                  var firstname="";
                  var lastname="";
                  for(var a=0;a<conv1.length;a++){
                      firstname=firstname.concat(String(conv1[a]+18));
                  }
                  for(var w=0;w<conv2.length;w++){
                      lastname=lastname.concat(String(conv2[w]+18));
                  }
                  res2[m][1]=firstname;
                  res2[m][2]=lastname;
              }
              //console.log(res2);
           }

          //review file preprocessing
          else if( this.$store.state.dataMapping.data.tableType=="1" ){
              //Softconf
              if(this.$store.state.dataMapping.data.formatType=="2"){
                var reviewres=[];
                reviewres.push(["Review Id","Submission Id", "Num Review Assignment", "Reviewer Name", "Expertise Level", "Review Comment","Confidence Level", "Overall Evaluation Score", "Column 9","Column 10","Column 11","Column 12", "Day of the Review Date", "Time of the Review Date", "Has Recommended for the Best Paper"]);

                for (var q = 1; q < res2.length; q++) {
                    var z = res2[q];
                    z[32]="confidence: "+z[32];
                    //console.log(typeof(z[7]));
                    //var str=z[7].toString();
                    var date_time=z[7].split(" ");
                    //console.log(date_time);
                    var date=date_time[0];
                    var time=date_time[1].split(":")[0]+":"+date_time[1].split(":")[1];
                    //console.log(date,time);
                    element=["",z[0],"","","",z[38],z[32],z[31],"","","","",date,time,"",verId];
                    reviewres.push(element);
                }
                res2=reviewres;
                //console.log(reviewres);
              }

              //author anonymization - JCDL
              // Easy Chair
              else if(this.$store.state.dataMapping.data.formatType=="1"){
                var convert_string=require("convert-string");
                for(var index=1;index<res2.length;index++){
                    var convert=convert_string.stringToBytes(res2[index][3]);
                    var name="";
                    for(var idx=0;idx<convert.length;idx++){
                        name=name.concat(String(convert[idx]+18));
                    }
                    res2[index][3]=name;
                }
              }
            }

           //ACL submission file processing
          else if( this.$store.state.dataMapping.data.tableType=="2" ){
              if(this.$store.state.dataMapping.data.formatType=="2"){
              var submissionres=[];
              submissionres.push(["#", "track #", "track name", "title", "authors", "submitted","last updated", "form fields", "keywords", "decision", "notified", "reviews sent", "abstract"]);

              for (var l = 1; l < res2.length; l++) {
                var y = res2[l];
                var dt = moment(y[10], "D MMM YYYY HH:mm:ss").format("YYYY-M-D H:m");
                if(y[6].includes("Reject")){y[6]="reject";}
                else {y[6]="accept";}
                //console.log(x);
                element=[y[0],"",y[4],y[2],y[3],dt,dt,"",y[13],y[6],"","",y[9], verId];
                submissionres.push(element);
              }
                res2=submissionres;
              }
          }

          if(this.$store.state.dataMapping.data.formatType=="1"){
            var tempCSV=[];
            //author
            if( this.$store.state.dataMapping.data.tableType=="0" ){
              tempCSV.push(["submission #","first name","last name","email","country","organization","Web page","person #","corresponding?"]);
            }
            //review
            else if(this.$store.state.dataMapping.data.tableType=="1"){
              tempCSV.push(["Review Id","Submission Id", "Num Review Assignment", "Reviewer Name", "Expertise Level", "Review Comment","Confidence Level", "Overall Evaluation Score", "Column 9","Column 10","Column 11","Column 12", "Day of the Review Date", "Time of the Review Date", "Has Recommended for the Best Paper"]);
            }
            //submission
            else if(this.$store.state.dataMapping.data.tableType=="2"){
              tempCSV.push(["#", "track #", "track name", "title", "authors", "submitted","last updated", "form fields", "keywords", "decision", "notified", "reviews sent", "abstract"]);
            }
            // for each row of data, manipulate temporary array element[] 
            // then push to true array res2[] for parsing
            var csvRow=[];
            for (var rowNum = 1; rowNum < res2.length; rowNum++) {
                csvRow = res2[rowNum];
                //csvRow.push(verId);
                tempCSV.push(csvRow);
            }
            res2=tempCSV;
          }
            //console.log(res2);
            this.$store.commit("setUploadedFile",res2);
            this.$store.commit("setPageLoadingStatus", false);
          }.bind(this)
        });
      }
    },
    components: {
      MappingTool
    }
  };
</script>

<style scoped>
  .upload-box {
    /*padding-top: 100px; */
  }

  .upload-box .el-select {
    margin-top: 20px;
  }

  .button-row {
    margin-top: 30px;
  }

  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  
  .clearfix:after {
    clear: both
  }

  .el-input {
    width: 207px;
  }

  .box-card {
    width: 480px;
    position: relative;
    left: 50%;
    margin-left: -240px;
  }

  .autocomplete-verid {
    position: relative;
  }

  .form-card {
    margin: 16px 0px;
  }

  .section {
    padding: 0px 16px 16px 16px;
  }
</style>
