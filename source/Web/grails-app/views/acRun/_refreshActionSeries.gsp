<g:select
    name="actionSeriesId"
    from="${actionSeries}"
    noSelection= "['': 'Select One...']"
    optionKey="id"
    value="${acRunTaskInstance?.actionSeries?.id}"
/>