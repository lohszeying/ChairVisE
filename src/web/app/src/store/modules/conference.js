import axios from 'axios'
import {deepCopy} from "@/common/utility"

export default {
  state: {
    conferenceList: [],
    versionList: [],
    conferenceListStatus: {
      isLoading: true,
      isApiError: false,
      apiErrorMsg: '',
    },
    conferenceForm: {
      id: '',
      name: '',
      version: '',
      description: '',
      creatorIdentifier: '',
    },

    conferenceFormStatus: {
      isLoading: false,
      isApiError: false,
      apiErrorMsg: '',
    },
    isSaveSuccess: false
  },
  mutations: {
    setConferenceListLoading(state, payload) {
      if (payload) {
        state.conferenceListStatus.isApiError = false;
      }
      state.conferenceListStatus.isApiError = payload;
    },
    setConferenceListApiError(state, payload) {
      state.conferenceListStatus.isApiError = true;
      state.conferenceListStatus.apiErrorMsg = payload;
    },
    setConferenceList(state, payload) {
      state.conferenceList = payload;
    },
    addToConferenceList(state, payload) {
      state.conferenceList.push(payload);
    },
    deleteFromConferenceList(state, payload) {
      const index = state.conferenceList.findIndex(conference => conference.id === payload);
      state.conferenceList.splice(index, 1)
    },

    //AFTER HERE
    updateConferenceListWith(state, payload) {
      state.conferenceList = state.conferenceList.map(conference => {
        if (conference.id === payload.id) {
          return payload
        }
        return conference
      });
    },

    setConferenceFormLoading(state, isLoading) {
      state.conferenceFormStatus.isLoading = isLoading;
    },

    setConferenceFormApiError(state, msg) {
      state.conferenceFormStatus.isApiError = true;
      state.conferenceFormStatus.apiErrorMsg = msg;
    },

    setConferenceForm(state, payload) {
      state.conferenceForm = payload;
    },

    resetConferenceForm(state) {
      state.conferenceForm.id = '';
      state.conferenceForm.name = '';
      state.conferenceForm.version = '';
      state.conferenceForm.description = '';
      state.conferenceForm.creatorIdentifier = '';
      state.conferenceFormStatus.isLoading = false;
      state.conferenceFormStatus.isApiError = false;
      state.conferenceFormStatus.apiErrorMsg = '';
    },

    setConferenceFormField(state, {field, value}) {
      state.conferenceForm[field] = value
    },

    setSaveSuccess(state, success) {
      state.isSaveSuccess = success;
    }
    /*
    setIsConferenceEditable(state, isPresentationEditable) {
      //May not need to use this
      state.isPresentationEditable = isPresentationEditable;
    }*/

    //Originally, this was in presentation
    /*setConferenceListLoading(state, payload) {
      if (payload) {
        state.presentationListStatus.isApiError = false;
      }
      state.presentationListStatus.isLoading = payload;
    },

    setConferenceListApiError(state, payload) {
      state.presentationListStatus.isApiError = true;
      state.presentationListStatus.apiErrorMsg = payload;
    },

    setConferenceList(state, payload) {
      state.presentationList = payload;
    },

    setVersionList(state, payload) {
      state.versionList = payload;
    },

    addToConferenceList(state, payload) {
      state.presentationList.push(payload);
    },

    deleteFromConferenceList(state, payload) {
      const index = state.presentationList.findIndex(presentation => presentation.id === payload);
      state.presentationList.splice(index, 1)
    },

    updateConferenceListWith(state, payload) {
      state.presentationList = state.presentationList.map(presentation => {
        if (presentation.id === payload.id) {
          return payload
        }
        return presentation
      });
    },

    setConferenceFormLoading(state, isLoading) {
      state.presentationFormStatus.isLoading = isLoading;
    },

    setConferenceFormApiError(state, msg) {
      state.presentationFormStatus.isApiError = true;
      state.presentationFormStatus.apiErrorMsg = msg;
    },

    setConferenceForm(state, payload) {
      state.presentationForm = payload;
    },

    resetConferenceForm(state) {
      state.presentationForm.id = '';
      state.presentationForm.name = '';
      state.presentationForm.version = '';
      state.presentationForm.description = '';
      state.presentationForm.creatorIdentifier = '';
      state.presentationFormStatus.isLoading = false;
      state.presentationFormStatus.isApiError = false;
      state.presentationFormStatus.apiErrorMsg = '';
    },

    setConferenceFormField(state, {field, value}) {
      state.presentationForm[field] = value
    },

    setSaveSuccess(state, success) {
      state.isSaveSuccess = success;
    },
    setIsConferenceEditable(state, isPresentationEditable) {
      //May not need to use this
      state.isPresentationEditable = isPresentationEditable;
    } */
  },
  actions: {
    async getConferenceList({commit}) {
      commit('setConferenceListLoading', true);
      axios.get('/api/conferences')
          .then(response => {
            commit('setConferenceList', response.data)
          })
          .catch(e => {
            commit('setConferenceListApiError', e.toString());
          })
          .finally(() => {
            commit('setConferenceListLoading', false);
          })
    },
    async getConference({commit}, conferenceId) {

      commit('setConferenceFormLoading', true);
      await axios.get(`/api/conferences/${conferenceId}`)
          .then(response => {
            commit('setConferenceForm', response.data)
          })
          .catch(e => {
            commit('setConferenceFormApiError', e.toString());
          })
          .finally(() => {
            commit('setConferenceFormLoading', false);
          });
    },

    async saveConference({commit, state}) {
      commit('setConferenceFormLoading', true);
      await axios.post('/api/conferences', state.presentationForm)
          .then(response => {
            commit('addToConferenceList', deepCopy(response.data));
            commit('setConferenceForm', deepCopy(response.data));
            commit("setSaveSuccess", true);
          })
          .catch(e => {
            commit('setConferenceFormApiError', e.toString());
            commit("setSaveSuccess", false);
          })
          .finally(() => {
            commit('setConferenceFormLoading', false);
          })
    },

    async updateConference({commit, state}) {
      commit('setConferenceFormLoading', true);
      await axios.put('/api/conferences/' + state.presentationForm.id, state.presentationForm)
          .then(response => {
            commit('updateConferenceListWith', response.data)
          })
          .catch(e => {
            commit('setConferenceFormApiError', e.toString());
          })
          .finally(() => {
            commit('setConferenceFormLoading', false);
          })
    },

    async deleteConference({commit}, payload) {
      commit('setConferenceFormLoading', true);
      await axios.delete('/api/conferences/' + payload)
          .then(() => {
            commit('deleteFromConferenceList', payload);
            commit('resetConferenceForm')
          })
          .catch(e => {
            commit('setConferenceFormApiError', e.toString());
          })
          .finally(() => {
            commit('setConferenceFormLoading', false);
          })
    }

    /*async getConferenceList({commit}) {
      commit('setPresentationListLoading', true);
      axios.get('/api/presentations')
        .then(response => {
          commit('setPresentationList', response.data)
        })
        .catch(e => {
          commit('setPresentationListApiError', e.toString());
        })
        .finally(() => {
          commit('setPresentationListLoading', false);
        })
    },

    async getVersionList({commit}) {
      //May or may not need to use this

      commit('setPresentationListLoading', true);
      axios.get('/api/version')
          .then(response => {
            commit('setVersionList', response.data)
          })
          .catch(e => {
            commit('setPresentationListApiError', e.toString());
          })
          .finally(() => {
            commit('setPresentationListLoading', false);
          })
    },

    async getConference({commit}, presentationId) {

      commit('setPresentationFormLoading', true);
      await axios.get(`/api/presentations/${presentationId}`)
          .then(response => {
            commit('setPresentationForm', response.data)
          })
          .catch(e => {
            commit('setPresentationFormApiError', e.toString());
          })
          .finally(() => {
            commit('setPresentationFormLoading', false);
          });
    },

    async saveConference({commit, state}) {
      commit('setPresentationFormLoading', true);
      await axios.post('/api/presentations', state.presentationForm)
          .then(response => {
            commit('addToPresentationList', deepCopy(response.data));
            commit('setPresentationForm', deepCopy(response.data));
            commit("setSaveSuccess", true);
          })
          .catch(e => {
            commit('setPresentationFormApiError', e.toString());
            commit("setSaveSuccess", false);
          })
          .finally(() => {
            commit('setPresentationFormLoading', false);
          })
    },

    async updateConference({commit, state}) {
      commit('setPresentationFormLoading', true);
      await axios.put('/api/presentations/' + state.presentationForm.id, state.presentationForm)
          .then(response => {
            commit('updatePresentationListWith', response.data)
          })
          .catch(e => {
            commit('setPresentationFormApiError', e.toString());
          })
          .finally(() => {
            commit('setPresentationFormLoading', false);
          })
    },

    async deleteConference({commit}, payload) {
      commit('setPresentationFormLoading', true);
      await axios.delete('/api/presentations/' + payload)
          .then(() => {
            commit('deleteFromPresentationList', payload);
            commit('resetPresentationForm')
          })
          .catch(e => {
            commit('setPresentationFormApiError', e.toString());
          })
          .finally(() => {
            commit('setPresentationFormLoading', false);
          })
    }*/
  }
};