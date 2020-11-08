<template>
  <el-collapse v-model="activeNames">
    <el-collapse-item title="Importing CSV Data" name="1">      
      <ol>
        <li>Log in.</li>
        <li>Click on "Add New Conference".</li>
        <li>Fill in the forms. </li>
        <li>Upload your CSV data. </li>
        <li>Map the CSV column headers</li>
        <li>Upload!</li>
      </ol>      
    </el-collapse-item>
    <el-collapse-item title="Manage conference" name="2">
      <ol>
        <li>Go to homepage.</li>
        <li>Click on a conference you just created when you imported data.</li>
        <li>Select the year the conference is held under "Select version".</li>
        <li>To add section to visualize data, click on "Add New Section" after selecting options from dropdown list.</li>
      </ol>
    </el-collapse-item>
  </el-collapse>  
</template>

<script>
  export default {
    name: "InstructionsGuide",
    data() {
      return {
        activeNames: []
      };
    },
  }
</script>

<style scoped>
</style>
