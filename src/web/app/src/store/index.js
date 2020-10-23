import Vue from 'vue'
import Vuex from 'vuex'
import userInfo from './modules/userInfo'
import mutations from './mutations'
import accessControl from './modules/accessControl'
import presentation from "./modules/presentation";
import conference from "./modules/conference";
import version from "./modules/version";
import section from "./modules/section";
import dbMetaData from "./modules/dbMetaData";
import dataMapping from "./modules/dataMapping";

Vue.use(Vuex);

export default new Vuex.Store({
  strict: process.env.NODE_ENV !== 'production',

  state: {
    isPageLoading: true,
  },

  mutations: mutations,

  actions: {},

  modules: {
    userInfo,
    presentation,
    conference,
    version,
    accessControl,
    section,
    dbMetaData,
    dataMapping
  }
})
