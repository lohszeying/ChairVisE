import axios from 'axios'
import {processMapping} from '@/store/helpers/processor.js'
import conference from "@/store/modules/conference";

export default {
  state: {
    hasDBSchemaSet: false,
    hasFileUploaded: false,
    hasFormatTypeSpecified: false,
    hasTableTypeSelected: false,
    hasConferenceTitleSpecified: false,
    hasVersionDateSpecified: false,
    hasHeaderSpecified: false,
    hasPredefinedSpecified: false,
    hasPredefinedSwitchSpecified: false, // new
    hasMappingFinished: false,
    isUploadSuccess: false,
    data: {
      dbSchema: null,
      uploadedData: [],
      uploadedLabel: [],
      mappingResult: [],
      processedResult: [],
      formatType: null,
      tableType: null,
      isNewConference: null,
      isNewVersion: null,
      conferenceTitle: null,
      versionDate: null,
      hasHeader: null,
      hasPredefined: null, // new
      predefinedMapping: null,
      predefinedMappingId: null,
    },
    error: []
  },

  mutations: {
    setUploadSuccess(state, success) {
      state.isUploadSuccess = success;
    },

    setUploadedFile(state, data) {
      state.data.uploadedLabel = data[0];
      state.data.uploadedData = data;
      state.hasFileUploaded = true;
    },

    clearUploadedFile(state) {
      state.data.uploadedLabel = [];
      state.data.uploadedData = [];
      state.hasFileUploaded = false;
    },

    setDBSchema(state, dbSchema) {
      state.data.dbSchema = dbSchema;
      state.hasDBSchemaSet = true;
    },

    clearDBSchema(state) {
      state.data.dbSchema = [];
      state.hasDBSchemaSet = false;
    },

    setFormatType(state, formatType) {
      state.data.formatType = formatType;
      state.hasFormatTypeSpecified = true;
    },

    clearFormatType(state) {
      state.data.formatType = null;
      state.hasFormatTypeSpecified = false;
    },

    setTableType(state, selected) {
      state.data.tableType = selected;  
      state.hasTableTypeSelected = true;
    },

    clearTableType(state) {
      state.data.tableType = null;
      state.hasTableTypeSelected = false;
    },

    setConferenceTitle(state, selected) {
      state.data.conferenceTitle = selected;
      state.hasConferenceTitleSpecified = true;
    },

    clearConferenceTitle(state) {
      state.data.conferenceTitle = null;
      state.hasConferenceTitleSpecified = false;
    },

    setIsNewConference(state, selected) {
      state.data.isNewConference = selected;
    },

    clearIsNewConference(state) {
      state.data.isNewConference = null;
    },

    setVersionDate(state, selected) {
      state.data.versionDate = selected;
      state.hasVersionDateSpecified = true;
    },

    clearVersionDate(state) {
      state.data.versionDate = null;
      state.hasVersionDateSpecified = false;
    },

    setIsNewVersion(state, selected) {
      state.data.isNewVersion = selected;
    },

    clearIsNewVersion(state) {
      state.data.isNewVersion = null;
    },

    setHasHeader(state, hasHeader) {
      state.data.hasHeader = hasHeader;
      state.hasHeaderSpecified = true;
    },

    clearHasHeader(state) {
      state.data.hasHeader = null;
      state.hasHeaderSpecified = false;
    },

    setPredefinedMapping(state, payload) {
      state.data.predefinedMapping = payload.mapping;
      state.data.predefinedMappingId = payload.id;
      state.hasPredefinedSpecified = true;
    },

    clearPredefinedMapping(state) {
      state.data.predefinedMapping = null;
      state.data.predefinedMappingId = null;
      state.hasPredefinedSpecified = false;
    },

    setPredefinedSwitch(state, hasPredefined) {
      state.data.hasPredefined = hasPredefined;
      state.hasPredefinedSwitchSpecified = true;
    },

    clearPredefinedSwitch(state) {
      state.data.hasPredefined = null;
      state.hasPredefinedSwitchSpecified = false;
    },

    setMapping(state, payload) {
      try {
        state.error = [];
        state.data.mappingResult = payload.map;
        state.mappingFinished = true;
        state.data.processedResult =
          processMapping(payload.map,
            state.data.uploadedData,
            state.data.dbSchema,
            state.data.hasHeader);
      } catch (err) {
        state.error.push(err);
        state.mappingFinished = false;
        state.data.mappingResult = [];
        state.data.processedResult = [];
      }
    },

    clearMapping(state) {
      state.data.mappingResult = [];
      state.data.processedResult = [];
      state.mappingFinished = false;
    },

    setDataMappingError(state, err) {
      state.error.push(err);
    },

    clearError(state) {
      state.error = [];
    }
  },

  actions: {
    async persistMappingNewVersion({commit, state}) {
      commit("setPageLoadingStatus", true);
      let endpoint;
      let fnKeyTable;
      switch (state.data.tableType) {
        case 0:
          endpoint = "author";
          fnKeyTable = "AuthorRecord";
          break;
        case 1:
          endpoint = "review";
          fnKeyTable = "ReviewRecord";
          break;
        case 2:
          endpoint = "submission";
          fnKeyTable = "SubmissionRecord";
          break;
      }
      var fnKeyEntry = {};
      fnKeyEntry.conferenceId = state.data.conferenceTitle;
      fnKeyEntry.versionId = state.data.versionDate;
      fnKeyEntry.recordType = fnKeyTable;
      
      // add version to end
      for (var i=0; i<state.data.processedResult.length; i++){
        var row = state.data.processedResult[i];
        row.conferenceId = state.data.conferenceTitle;
        row.versionId = state.data.versionDate;
      }

      // concurrent POST data and POST version requests 
      axios.all([postTable(endpoint, state.data.processedResult), postVersion(fnKeyEntry)])  
        .then(axios.spread( () => {
          commit("setPageLoadingStatus", false);
          commit("setUploadSuccess", true);
        }))
        .catch(axios.spread(function (dataErr, verErr) {
          commit("setPageLoadingStatus", false);
          commit("setUploadSuccess", false);
          commit("setDataMappingError", dataErr);
          commit("setDataMappingError", verErr);
        }));
    },

    async persistMappingOldVersion({commit, state}) {
      commit("setPageLoadingStatus", true);
      let endpoint;
      switch (state.data.tableType) {
        case 0:
          endpoint = "author";
          break;
        case 1:
          endpoint = "review";
          break;
        case 2:
          endpoint = "submission";
          break;
      }
      // add version to end
      for (var i=0; i<state.data.processedResult.length; i++){
        var row = state.data.processedResult[i];
        row.conferenceId = state.data.conferenceTitle;
        row.versionId = state.data.versionDate;
      }
      //console.log(state.data.processedResult);
      await axios.post("/api/record/" + endpoint, state.data.processedResult)
        .then(() => {
          commit("setPageLoadingStatus", false);
          commit("setUploadSuccess", true);
        })
        .catch(e => {
          commit("setPageLoadingStatus", false);
          commit("setUploadSuccess", false);
          commit("setDataMappingError", e.toString());
        });
    }
  }
}

function postConference(fnKeyEntry) {
  return axios.post()
}
function postVersion(fnKeyEntry) {
  /*this.$store.dispatch('saveConference').then(() => {
    if (this.isNewConference && !this.isLogin) {
      return axios.post(`/api/conferences/${conferenceId}/versions`, fnKeyEntry);
    }
  }) */
  //console.log("fnKeyEntry: version ID: " + fnKeyEntry.versionId);

  return axios.post("/api/version", fnKeyEntry);
}

function postTable(endpoint, processedResult) {
  return axios.post("/api/record/" + endpoint, processedResult);
}