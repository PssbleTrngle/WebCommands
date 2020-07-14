<template>
  <div id="app">
    <Console />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import Console from "./components/Console.vue";
import "reset-css";

export const EventHub = new Vue();

@Component({
  components: { Console }
})
export default class App extends Vue {}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  display: grid;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: #565a6b;
}
</style>
