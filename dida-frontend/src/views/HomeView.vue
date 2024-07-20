<!--内容-->
<template>
  <div class="home">
    <h1>{{ a }}</h1>
  </div>
</template>

<!--行为-->
<script setup lang="ts">
const a = 1;
</script>

<!--样式-->
<style scoped>
#home {
  padding: 20px;
  background-color: #f0f0f0;
}
</style>
