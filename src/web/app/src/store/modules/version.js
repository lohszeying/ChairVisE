import axios from 'axios'
import {deepCopy} from "@/common/utility"

export default {
  state: {
    versionList: [],
    versionForm: {
      id: '',
      ver_date: '',
      creatorIdentifier: '',
    },
    versionListStatus: {
      isLoading: true,
      isApiError: false,
      apiErrorMsg: '',
    },
    versionFormStatus: {
      isLoading: false,
      isApiError: false,
      apiErrorMsg: '',
    },
    isSaveSuccess: false,
  },
  mutations: {
    setVersionListLoading(state, payload) {
      if (payload) {
        state.versionListStatus.isApiError = false;
      }
      state.versionListStatus.isLoading = payload;
    },

    setVersionListApiError(state, payload) {
      state.versionListStatus.isApiError = true;
      state.versionListStatus.apiErrorMsg = payload;
    },

    setVersionFormLoading(state, isLoading) {
      state.versionFormStatus.isLoading = isLoading;
    },

    setVersionFormApiError(state, msg) {
      state.versionFormStatus.isApiError = true;
      state.versionFormStatus.apiErrorMsg = msg;
    },

    setVersionList(state, payload) {
      state.versionList = payload;
    },

    addToVersionList(state, payload) {
      console.log(payload);
      state.versionList.push(payload);
    },

    deleteFromVersionList(state, payload) {
      const index = state.versionList.findIndex(version => version.id === payload);
      state.versionList.splice(index, 1)
    },

    updateVersionListWith(state, payload) {
      state.versionList = state.versionList.map(version => {
        if (version.id === payload.id) {
          return payload
        }
        return version
      });
    },
    setVersionForm(state, payload) {
      state.versionForm = payload;
    },

    resetVersionForm(state) {
      state.versionForm.id = '';
      state.versionForm.ver_date = '';
      state.versionForm.creatorIdentifier = '';
      state.versionFormStatus.isLoading = false;
      state.versionFormStatus.isApiError = false;
      state.versionFormStatus.apiErrorMsg = '';
    },
    setSaveSuccess(state, success) {
      state.isSaveSuccess = success;
    },
    setVersionFormField(state, {field, value}) {
      state.versionForm[field] = value
    },
  },

  actions: {
    async getVersionList({commit}, conferenceId) {
      axios.get(`/api/conferences/${conferenceId}/versions`)
          .then(response => {
            commit('setVersionList', response.data)
          })
          .catch(e => {
            commit('setVersionListApiError', e.toString());
          })
          .finally(() => {
            commit('setVersionListLoading', false);
          })
    },
    async getVersion({commit}, {conferenceId, versionId}) {
      commit('setVersionFormLoading', true);
      await axios.get(`/api/conferences/${conferenceId}/versions/${versionId}`)
          .then(response => {
            commit('setVersionForm', response.data)
          })
          .catch(e => {
            commit('setVersionFormApiError', e.toString());
          })
          .finally(() => {
            commit('setVersionFormLoading', false);
          });
    },

    async updateVersion({commit, state}, conferenceId) {
      commit('setVersionFormLoading', true);
      await axios.put(`/api/conferences/${conferenceId}/versions/` + state.version.id, state.versionForm)
          .then(response => {
            commit('updateVersionListWith', response.data)
          })
          .catch(e => {
            commit('setVersionFormApiError', e.toString());
          })
          .finally(() => {
            commit('setVersionFormLoading', false);
          })
    },

    async saveVersion({commit, state}, conferenceId) {
      console.log("inside version.js, version form: " + state.versionForm.ver_date);
      commit('setVersionFormLoading', true);
      await axios.post(`/api/conferences/${conferenceId}/versions`, state.versionForm)
          .then(response => {
            //response.data.date = state.versionForm.ver_date;
            //response.data.date = new Date(state.versionForm.ver_date);
            //response.data.ver_date = new Date(state.versionForm.ver_date).toISOString().slice(0,10);
            //response.data.ver_date = new Date(2020,1,2).toISOString().slice(0,10);
            console.log("In version.js: ");
            console.log(state.versionForm);
            console.log(response.data);

            commit('addToVersionList', deepCopy(response.data));
            commit('setVersionForm', deepCopy(response.data))
            commit("setSaveSuccess", true);
          })
          .catch(e => {
            commit('setVersionFormApiError', e.toString());
            commit("setSaveSuccess", false);
          })
          .finally(() => {
            commit('setVersionFormLoading', false);
          })
    },

    async deleteVersion({commit}, payload, conferenceId) {
      commit('setVersionFormLoading', true);
      await axios.delete(`/api/conferences/${conferenceId}/versions/` + payload)
          .then(() => {
            commit('deleteFromVersionList', payload);
            commit('resetVersionForm')
          })
          .catch(e => {
            commit('setVersionFormApiError', e.toString());
          })
          .finally(() => {
            commit('setVersionFormLoading', false);
          })
    }
  }
};
