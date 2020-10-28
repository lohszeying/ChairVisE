<template>
    <div class="emptySection">
        <el-row>
            <el-col>
                <div class="icon">
                    <img src="@/assets/businessman.png" />
                </div>
            </el-col>
        </el-row>
        <el-row>
            <el-col>
                <p> 
                    <i> No conference found. Want to create a conference? </i>
                </p>
                <p>
                    Let's get started by adding a new conference.
                </p>
                <el-button type="primary" icon="el-icon-plus" @click="createConference">Add New Conference</el-button>
            </el-col>
        </el-row>
    </div>
</template>

<script>
export default {
    methods: {
        createConference() {
            //this.$router.push("/manage/create");
            this.$router.push("/importData");
        },
    }    
}
</script>

<style scoped>
  .icon {
    margin-left: 04.5rem;
    margin-bottom: 0.25rem;
  }
  .emptySection {
    text-align: center;
    vertical-align: middle;
  }
</style>