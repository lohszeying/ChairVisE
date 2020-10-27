import axios from 'axios'
import {deepCopy} from "@/common/utility"

export default {
    state: {
        recordList: [],
        authorRecordForm: {
            submission_id: '',
            first_name: '',
            last_name: '',
            email: '',
            country: '',
            organization: '',
            web_page: '',
            person_id: '',
            is_corresponding: ''
        },
        submissionRecordForm: {
            s_id: '',
            track_id: '',
            track_name: '',
            title: '',
            submitted: '',
            last_updated: '',
            keywords: '',
            decision: '',
            notified: '',
            reviews_sent: '',
            abstract: ''
        },
        reviewRecordForm: {
            review_id: '',
            submission_id: '',
            review_assignment_num: '',
            reviewer_name: '',
            field: '',
            review_comment: '',
            overall_eval_score: '',
            review_submission_date: '',
            review_submission_time: '',
            best_paper_recommendation: ''
        },
        recordListStatus: {
            isLoading: true,
            isApiError: false,
            apiErrorMsg: '',
        },
        authorRecordFormStatus: {
            isLoading: false,
            isApiError: false,
            apiErrorMsg: '',
        },
        submissionRecordFormStatus: {
            isLoading: false,
            isApiError: false,
            apiErrorMsg: '',
        },
        reviewRecordFormStatus: {
            isLoading: false,
            isApiError: false,
            apiErrorMsg: '',
        },
        isSaveSuccess: false,
    },
    mutations: {
        setRecordListLoading(state, payload) {
            if (payload) {
                state.recordListStatus.isApiError = false;
            }
            state.recordListStatus.isLoading = payload;
        },
        setRecordListApiError(state, payload) {
            state.recordListStatus.isApiError = true;
            state.recordListStatus.apiErrorMsg = payload;
        },
        setRecordList(state, payload) {
            state.recordList = payload;
        },
        setAuthorRecordFormLoading(state, isLoading) {
            state.authorRecordFormStatus.isLoading = isLoading;
        },
        setAuthorRecordFormApiError(state, msg) {
            state.authorRecordFormStatus.isApiError = true;
            state.authorRecordFormStatus.apiErrorMsg = msg;
        },
        setAuthorRecordForm(state, payload) {
            state.authorRecordForm = payload;
        },
        setSubmissionRecordFormLoading(state, isLoading) {
            state.submissionRecordFormStatus.isLoading = isLoading;
        },
        setSubmissionRecordFormApiError(state, msg) {
            state.submissionRecordFormStatus.isApiError = true;
            state.submissionRecordFormStatus.apiErrorMsg = msg;
        },
        setSubmissionRecordForm(state, payload) {
            state.submissionRecordForm = payload;
        },
        setReviewRecordFormLoading(state, isLoading) {
            state.reviewRecordFormStatus.isLoading = isLoading;
        },
        setReviewRecordFormApiError(state, msg) {
            state.reviewRecordFormStatus.isApiError = true;
            state.reviewRecordFormStatus.apiErrorMsg = msg;
        },
        setReviewRecordForm(state, payload) {
            state.reviewRecordForm = payload;
        },
        setSaveSuccess(state, success) {
            state.isSaveSuccess = success;
        },

    },

    actions: {
        async getRecordList({commit}, versionId) {
            axios.get(`/api/versions/{versionId}`)
                .then(response => {
                    commit('setRecordList', response.data)
                })
                .catch(e => {
                    commit('setRecordListApiError', e.toString());
                })
                .finally(() => {
                    commit('setRecordListLoading', false);
                })
        },

        async saveAuthorRecord({commit, state}, versionId) {
            commit('setAuthorRecordFormLoading', true);
            await axios.post(`/api/versions/{versionId}/authorRecord`, state.authorRecordForm)
                .then(response => {
                    console.log("In record.js: ");
                    console.log(state.authorRecordForm);
                    console.log(response.data);
                    commit('setAuthorRecordForm', deepCopy(response.data));
                    commit("setSaveSuccess", true);
                })
                .catch(e => {
                    commit('setAuthorRecordFormApiError', e.toString());
                    commit("setSaveSuccess", false);
                })
                .finally(() => {
                    commit('setAuthorRecordFormLoading', false);
                })
        },

        async saveSubmissionRecord({commit, state}, versionId) {
            commit('setRecordFormLoading', true);
            await axios.post(`/api/versions/{versionId}/submissionRecord`, state.submissionRecordForm)
                .then(response => {
                    console.log("In record.js: ");
                    console.log(state.submissionRecordForm);
                    console.log(response.data);
                    commit('setSubmissionRecordForm', deepCopy(response.data));
                    commit("setSaveSuccess", true);
                })
                .catch(e => {
                    commit('Submission', e.toString());
                    commit("setSaveSuccess", false);
                })
                .finally(() => {
                    commit('setSubmissionRecordFormLoading', false);
                })
        },

        async saveReviewRecord({commit, state}, versionId) {
            commit('setRecordFormLoading', true);
            await axios.post(`/api/versions/{versionId}/reviewRecord`, state.reviewRecordForm)
                .then(response => {
                    console.log("In record.js: ");
                    console.log(state.reviewRecordForm);
                    console.log(response.data);
                    commit('setReviewRecordForm', deepCopy(response.data));
                    commit("setSaveSuccess", true);
                })
                .catch(e => {
                    commit('setReviewRecordFormApiError', e.toString());
                    commit("setSaveSuccess", false);
                })
                .finally(() => {
                    commit('setReviewRecordFormLoading', false);
                })
        }


    }
};
