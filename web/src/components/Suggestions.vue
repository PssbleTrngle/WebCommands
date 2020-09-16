<template>
  <div id="suggestions">
    <span class="hidden">{{ padding }}</span>
    <span class="suggestion" v-if="suggestions.length === 1">{{ suggestions[0].text }}</span>
    <div id="selection" v-else>
      <span
        v-for="(suggestion, i) in suggestions"
        :key="suggestion.text"
        :class="{ selected: i === selected }"
      >{{ suggestion.text }}</span>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import { EventHub } from "../main";
import { clamp } from "lodash";

export interface Suggestion {
  start: number;
  end: number;
  text: string;
  tooltip?: string;
}

interface Check {
  errors: any[];
  suggestions: Suggestion[];
}

@Component({
  created: function(this: Suggestions) {
    EventHub.$on("check", (check: Check) => {
      console.log(check)
      this.suggestions = check.suggestions ?? [];
      this.selected = -1;
    });
  },
  computed: {
    padding: function(this: Suggestions) {
      const end = this.suggestions[0]?.start ?? this.value.length;
      return this.value.substring(0, end);
    }
  }
})
export default class Suggestions extends Vue {
  @Prop()
  value!: string;

  private selected = -1;
  private suggestions: Suggestion[] = [];

  public tab() {
    this.selected = (this.selected + 1) % this.suggestions.length;
    this.$emit("complete", this.suggestions[this.selected]);
  }
}
</script>

<style scoped lang="scss">
#suggestions {
  position: absolute;
  top: 0;
  left: 0;
  pointer-events: none;
  user-select: none;

  display: grid;
  grid-auto-flow: column;

  #selection {
    display: grid;
    grid-auto-flow: row;
    transform: translateY(-100%);
    gap: 5px;

    span {
      &:not(.selected) {
        opacity: 0.5;
      }
    }
  }

  span {
    &.hidden {
      opacity: 0;
    }

    &.suggestion {
      opacity: 0.5;
      font-style: italic;
    }
  }
}
</style>
